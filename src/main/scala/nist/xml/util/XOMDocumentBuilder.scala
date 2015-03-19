package nist.xml.util

import java.io.InputStream

import scala.util.Failure
import scala.util.Success
import scala.util.Try

import org.w3c.dom.ls.LSResourceResolver
import org.xml.sax.ErrorHandler
import org.xml.sax.SAXParseException
import org.xml.sax.XMLReader

import javax.xml.XMLConstants
import javax.xml.parsers.SAXParserFactory
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.SchemaFactory
import nu.xom.Builder
import nu.xom.Document
import nu.xom.ParsingException
import nu.xom.ValidityException

/**
  * @author Salifou Sidi M. Malick <salifou.sidi@gmail.com>
  */

object XOMDocumentBuilder {

    /**
    * Build the nu.xom.Document representing the xml and validate against the schema 
    */
  def build(xml: InputStream, xsd: InputStream, resolver: Option[LSResourceResolver] = None): Try[Document] =
    build(xml)
    /*createXMLReader( xsd, resolver ) match {
      case Success(reader) =>
        try {
          // when using new Builder( reader, true ) the code fails with "Document is invalid: no grammar found."
          // It seems like it is trying to validate using internal DTD or schema
          val builder = new Builder( reader, false )
          Success(builder.build(xml))
        } catch { 
          case e: ValidityException => Failure( new Error( msg(e) ) )
          case e: ParsingException  => Failure( new Error( msg(e) ) )
          case e: Exception => Failure(e) 
        }
      case Failure(e) => Failure( new Error(s"[Error] Unable to create the XML Reader. ${e.getMessage}"))
    }*/

  /**
    * Build the nu.xom.Document representing the xml
    */
  def build(xml: InputStream): Try[Document] =
    try {
      val builder = new Builder
      Success(builder.build(xml))
    } catch { 
       case e: ValidityException => Failure( new Error( msg(e) ) )
       case e: ParsingException  => Failure( new Error( msg(e) ) )
       case e: Exception => Failure(e) 
    }

  private def createXMLReader( xsd: InputStream, resolver: Option[LSResourceResolver] = None ): Try[XMLReader] = 
    try {
      // Create the schema
      val factory =  SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
      resolver match { case Some(r) => factory.setResourceResolver( r ); case _ => }
      val schema = factory.newSchema( new StreamSource(xsd) )
      // Setup SAX parser for schema validation
      val spf = SAXParserFactory.newInstance()
      spf.setNamespaceAware(true)
      // For some reasons when this is set the code is failing with 'Document is invalid: no grammar found.'
      // It seems like it is expecting an internal DTD or schema
      // spf.setValidating(true)
      spf.setSchema(schema)
      val parser = spf.newSAXParser
      val reader = parser.getXMLReader
      // Setup the error handler
      reader.setErrorHandler( new CustomErrorHandler )
      Success( reader )
    } catch { case e: Exception => Failure( e ) }

  def msg(ve: ValidityException) = {
    val nbError = ve.getErrorCount
    val details = (0 until nbError) map { i =>
      s"${ve.getValidityError(i)} At line ${ve.getLineNumber(i)}, column ${ve.getColumnNumber(i)}" 
    } mkString("\t- ","\n\t- ", "")
    s"The document is invalid. ${nbError} error(s) found.\n  ${ details }"
  }

  def msg(pe: ParsingException) =
    s"The document is invalid.\n\t[Error] ${pe.getMessage} At line ${pe.getLineNumber}, column ${pe.getColumnNumber} "
}

class CustomErrorHandler extends ErrorHandler { 

  def warning(e: SAXParseException) = throw e // fail fast

  def error(e: SAXParseException) = throw e // fail fast

  def fatalError(e: SAXParseException) = throw e // fail fast
}