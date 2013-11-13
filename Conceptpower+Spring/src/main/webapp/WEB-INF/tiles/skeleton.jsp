<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html>
<head>

	<title><tiles:insertAttribute name="title" /></title>
	<link rel="stylesheet"	href="${pageContext.servletContext.contextPath}/resources/css/style.css" />
	<tiles:insertAttribute name="header" />
</head>
<body>
	<tiles:importAttribute name="currentPage" scope="request" />
	<div id="main">
	<div id="header">
	<div id="logo">
		<div id="logo_text"><!-- class="logo_colour", allows you to change the colour of the text -->
			<h1><a href="index.html">Concept<span class="logo_colour">power</span></a></h1>
			<h2>dHPS</h2>
		</div>
	</div>
	
	<div id="menubar">
		<tiles:insertAttribute name="navigation" />
	</div>
	</div>
	
	
	
	<div id="content_header"></div>
	<div id="site_content">
		<div class="sidebar">
			<tiles:insertAttribute name="sidebar" />
			<h2></h2>
		</div>
	
		<div id="content">
			<tiles:insertAttribute name="content" />
		</div>
		
		<div id="content_footer"></div>
		<div id="footer">
			Copyright &#169; simplestyle_1 | <a href="http://validator.w3.org/check?uri=referer">HTML5</a> | <a href="http://jigsaw.w3.org/css-validator/check/referer">CSS</a> | <a href="http://www.html5webtemplates.co.uk">design from HTML5webtemplates.co.uk</a> | <a href="http://salleedesign.com">icons by Jeremy Sallee</a>
		</div>
	</div>
</div>
</body>
</html>