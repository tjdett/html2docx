package au.edu.uq.html2docx

import org.scalatra._
import scalate.ScalateSupport

class MainServlet extends Html2docxDemoStack {

  get("/") {
    <html>
      <body>
        <h1>Hello, world!</h1>
        Convert test HTML <a href="test">to DOCX</a>.
      </body>
    </html>
  }

  get("/test") {
    ""
  }

}
