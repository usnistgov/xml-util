package nist.xml.util

import java.io.BufferedInputStream
import java.io.File
import java.io.InputStream
import java.io.Reader

import scala.beans.BeanProperty
import scala.io.Source

import org.w3c.dom.ls.LSInput
import org.w3c.dom.ls.LSResourceResolver

/**
  * Resolve included/imported XSD schemas.
  * 
  * Notes:
  *   1) The schemas are expected to be in the specified folder in the class path
  *   2) The schemaLocation should only contain the name of the file to be included
  */

/**
  * @author Salifou Sidi M. Malick <salifou.sidi@gmail.com>
  */

class ClassPathResourceResolver( val folder: String ) extends LSResourceResolver {

  /**
    * Resolve a reference to a resource
    * @param type The type of resource, for example a schema, source XML document, or query
    * @param namespace The target namespace (in the case of a schema document)
    * @param publicId The public ID
    * @param systemId The system identifier (as written, possibly a relative URI)
    * @param baseURI The base URI against which the system identifier should be resolved
    * @return an LSInput object typically containing the character stream or byte stream identified
    * by the supplied parameters; or null if the reference cannot be resolved or if the resolver chooses
    * not to resolve it.
    */
  def resolveResource( `type`: String, namespace: String, publicId: String, systemId: String, baseURI: String): LSInput = {
    val resourceAsStream = getClass().getResourceAsStream( folder + File.separator + systemId )
    return new Input(publicId, systemId, resourceAsStream)
  }
}

class Input( 
    @BeanProperty var publicId: String,
    @BeanProperty var systemId: String,
    @BeanProperty var input: InputStream
  ) extends LSInput {

  private val inputStream = new BufferedInputStream ( input )
  
  @BeanProperty var baseURI: String = null

  @BeanProperty var byteStream: InputStream = null

  @BeanProperty var certifiedText: Boolean = false

  @BeanProperty var characterStream: Reader = null

  @BeanProperty var encoding: String = null

  @BeanProperty var stringData: String = Source.fromInputStream(input).getLines().mkString
}