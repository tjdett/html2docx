package au.edu.uq.html2docx

import org.scalatra._
import scalate.ScalateSupport
import org.docx4j.openpackaging.packages.WordprocessingMLPackage
import org.docx4j.convert.in.xhtml.XHTMLImporter
import java.io.FileInputStream
import java.io.StringReader
import scala.xml.XML

class MainServlet extends Html2docxDemoStack {

  get("/") {
    <html>
      <link href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css" rel="stylesheet"/>
      <link rel="stylesheet" href="/css/medium.css"/>
      <script src="http://code.jquery.com/jquery-1.10.1.min.js"></script>
      <script src="//netdna.bootstrapcdn.com/bootstrap/3.0.0/js/bootstrap.min.js"></script>
      <script src="/js/medium.js"></script>
      <body>
        <div class="container">
          <div class="row">
            <div class="col-lg-12">
            <h1>Start Writing...</h1>
            <hr/>
            <div id="editor">Replace this content.</div>
            <hr/>
            <p><b>Bold = ctrl + b</b>, <i>Italic = ctrl + i</i></p>
            <form id="docx" method="POST" action="/test.docx">
              <input type="hidden" name="html" value=""/>
              <button id="dl" class="btn btn-default">Download as Word</button>
            </form>
            </div>
          </div>
        </div>
        <script type="text/javascript">
        <![CDATA[
        new Medium({
          debug: false,
          autofocus: true,
          autoHR: false,
          element: document.getElementById('editor'),
          mode: 'rich'
        });
        $(function() {
          $('#dl').click(function() {
            $('#docx input[name=html]').val($('#editor').html().trim());
            $('#docx').submit();
          });
        });
        ]]>
        </script>
      </body>
    </html>
  }

  post("/test.docx") {
    contentType =
      "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    val wordMLPackage = WordprocessingMLPackage.createPackage()

    val xhtml: String = XML.loadString("<div>"+params("html")+"</div>")+""

    wordMLPackage.getMainDocumentPart()
      .getContent().addAll(XHTMLImporter.convert(xhtml, null, wordMLPackage))

    val tf = java.io.File.createTempFile("word_tmp_", "")
    wordMLPackage.save(tf)
    Ok(new FileInputStream(tf) {
      override def close = {
        super.close
        tf.delete()
      }
    })
  }

}
