<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html>
<head>

<title><tiles:insertAttribute name="title" /></title>
<link rel="stylesheet"
	href="${pageContext.servletContext.contextPath}/resources/css/style.css" />
<link rel="stylesheet"
	href="${pageContext.servletContext.contextPath}/resources/txt-layout/css/jquery-ui.css" />
<link rel="stylesheet"
	href="${pageContext.servletContext.contextPath}/resources/txt-layout/css/jquery.dataTables_themeroller.css" />
<link rel="stylesheet"
	href="${pageContext.servletContext.contextPath}/resources/txt-layout/css/demo_table_jui.css" />
<link
	href="http://fonts.googleapis.com/css?family=Open+Sans:400,700|Open+Sans+Condensed:700"
	rel="stylesheet" />
<script
	src="${pageContext.servletContext.contextPath}/resources/txt-layout/js/jquery-1.9.1.min.js"></script>
<script
	src="${pageContext.servletContext.contextPath}/resources/txt-layout/js/skel.min.js"></script>
<script
	src="${pageContext.servletContext.contextPath}/resources/txt-layout/js/skel-ui.min.js"></script>
<script
	src="${pageContext.servletContext.contextPath}/resources/txt-layout/js/jit.js"></script>
<script
	src="${pageContext.servletContext.contextPath}/resources/txt-layout/js/ex1.js"></script>
<script
	src="${pageContext.servletContext.contextPath}/resources/txt-layout/js/ex2.js"></script>

<script
	src="${pageContext.servletContext.contextPath}/resources/txt-layout/js/jquery-alert.js"></script>
<script
	src="${pageContext.servletContext.contextPath}/resources/txt-layout/js/jquery.dataTables.js"></script>
<script
	src="${pageContext.servletContext.contextPath}/resources/txt-layout/js/jquery.quick.pagination.min.js"></script>
<script
	src="${pageContext.servletContext.contextPath}/resources/txt-layout/js/jquery-ui.js"></script>
<noscript>
	<link rel="stylesheet"
		href="${pageContext.servletContext.contextPath}/resources/txt-layout/css/skel-noscript.css" />
	<link rel="stylesheet"
		href="${pageContext.servletContext.contextPath}/resources/css/style.css" />
	<link rel="stylesheet"
		href="${pageContext.servletContext.contextPath}/resources/txt-layout/css/style-desktop.css" />
</noscript>
<tiles:insertAttribute name="header" />
</head>
<body>
	<tiles:importAttribute name="currentPage" scope="request" />
	<div id="main">
		<div id="header">
			<div id="logo">
				<div id="logo_text">
					<!-- class="logo_colour", allows you to change the colour of the text -->
					<h1>
						<a href="${pageContext.servletContext.contextPath}/">Concept<span
							class="logo_colour">power</span></a>
					</h1>
					<h2>dHPS</h2>
				</div>
			</div>

			<div id="menubar">
				<tiles:insertAttribute name="navigation" />
			</div>
		</div>



		<div id="content_header"></div>

		<div id="site_content">

			<div id="content" style="width: 75%; float: left;">
				<tiles:insertAttribute name="content" />
			</div>

			<div class="sidebar" style="width: 20%; float: right;">
				<tiles:insertAttribute name="sidebar" />
				<h2></h2>
			</div>


		</div>

		<div id="content_footer"></div>
		<div id="footer">
			<p class="legal">Pull Request: ${pullrequest}</p>
			<p class="legal">Version: ${buildNumber}</p>
			Copyright &#169; simplestyle_1 | <a
				href="http://validator.w3.org/check?uri=referer">HTML5</a> | <a
				href="http://jigsaw.w3.org/css-validator/check/referer">CSS</a> | <a
				href="http://www.html5webtemplates.co.uk">design from
				HTML5webtemplates.co.uk</a> | <a href="http://salleedesign.com">icons
				by Jeremy Sallee</a>
		</div>

	</div>
</body>
</html>