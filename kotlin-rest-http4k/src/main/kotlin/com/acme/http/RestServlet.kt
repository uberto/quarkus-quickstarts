package com.acme.http

import javax.servlet.Servlet
import javax.servlet.annotation.WebServlet



@WebServlet("/")
open class RestServlet: Servlet by Http4kServlet(fruitsHandler)