package nist.xml.util

import nu.xom.Element
import nu.xom.Elements

/**
  * XOM Objects extensions
  */

/**
  * @author Salifou Sidi M. Malick <salifou.sidi@gmail.com>
  */

object XOMExtensions {

  /**
    * Extends xom.Element by adding `attribute' method which returns the 
    * value of the attribute or empty string the element doesn't that attribute 
    */
  implicit class ExtendedElement( val e: Element ) extends AnyVal {
    def attribute( name: String ): String = {
      val att = e.getAttribute( name )
      if( att == null ) "" else att.getValue
    }
  }

  /**
    * Extends xom.Elements by adding higher order functions
    */ 
  implicit class ExtendedElements( val elems: Elements ) extends AnyVal {

    def view = ( 0 until elems.size ).view map ( i => elems.get( i ) )

    def map[T]( f: Element => T ): Seq[T] = ( view map f ).force

    def filter( f: Element => Boolean ): Seq[Element] = ( view filter f ).force

    def foldLeft[T](z: T)(op: (T, Element) ⇒ T): T = view.foldLeft(z)(op)

    def partition( f: Element => Boolean ): ( Seq[Element], Seq[Element] ) = view.force partition f 

    def forall( f: Element => Boolean ): Boolean = view forall f

    def foreach( f: Element => Unit ): Unit = view foreach f
  }
}