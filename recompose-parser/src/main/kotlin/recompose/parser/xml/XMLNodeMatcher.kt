package recompose.parser.xml

import com.jds.recompose.nodes.Node
import com.jds.recompose.nodes.UnknownNode
import io.github.classgraph.ClassGraph
import org.xmlpull.v1.XmlPullParser
import recompose.parser.NodeMatcher
import recompose.parser.xml.transformer.nodes.NodeTransformer
import recompose.parser.xml.transformer.nodes.UnknownNodeTransformer

object XMLNodeMatcher : NodeMatcher<XmlPullParser> {
    private val transformer =
        ClassGraph().enableClassInfo().scan()
            .getClassesImplementing(NodeTransformer::class.java)
//            .filter { it.typeDescriptor.typeParameters.any { it.name == XmlPullParser::class.java.simpleName } }
            .loadClasses()
            .map { it.getDeclaredConstructor().newInstance() }

    private val unknownNodeTransformer =
        transformer.firstOrNull { it.javaClass == UnknownNodeTransformer::class.java } as NodeTransformer<UnknownNode, XmlPullParser>?

    override fun match(parser: XmlPullParser): Node? {
        parser.require(XmlPullParser.START_TAG, null, null)
        return transformer.firstOrNull { (it as NodeTransformer<*, *>).matches(parser.name) }?.let {
            (it as NodeTransformer<*, XmlPullParser>).toAstNode(parser)
        } ?: unknownNodeTransformer?.toAstNode(parser)
    }
}
