<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<meta name="viewport"    content="width=device-width, initial-scale=1.0">
	<meta name="description" content="">
	<meta name="author"      content="Sergey Pozhilov (GetTemplate.com)">
	
	<title>Conceptpower</title>

	<link rel="shortcut icon" href="${pageContext.servletContext.contextPath}/resources/assets/images/logo_cp.png">
	
	<link rel="stylesheet" href="${pageContext.servletContext.contextPath}/resources/css/additions.css">	
	<link rel="stylesheet" media="screen" href="http://fonts.googleapis.com/css?family=Open+Sans:300,400,700">
	<link rel="stylesheet" href="${pageContext.servletContext.contextPath}/resources/assets/css/bootstrap.min.css">
	<!--  <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/resources/assets/css/font-awesome.min.css">-->
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">
	<!-- Custom styles for our template -->
	<link rel="stylesheet" href="${pageContext.servletContext.contextPath}/resources/assets/css/bootstrap-theme.css" media="screen" >
	<link rel="stylesheet" href="${pageContext.servletContext.contextPath}/resources/assets/css/main.css">
	
	
	<link rel="stylesheet" href="${pageContext.servletContext.contextPath}/resources/js/datatable/css/dataTables.bootstrap.min.css" />
	
	<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
	<!--[if lt IE 9]>
	<script src="${pageContext.servletContext.contextPath}/resources/assets/js/html5shiv.js"></script>
	<script src="${pageContext.servletContext.contextPath}/resources/assets/js/respond.min.js"></script>
	<![endif]-->
	
	
	<tiles:insertAttribute name="header" />
</head>

<body>
<!-- JavaScript libs are placed at the end of the document so the pages load faster -->
	<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
	  <script src="http://code.jquery.com/jquery-1.10.2.js"></script>
	  <script src="http://code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
	
	<script src="http://netdna.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
	<script src="${pageContext.servletContext.contextPath}/resources/assets/js/headroom.min.js"></script>
	<script src="${pageContext.servletContext.contextPath}/resources/assets/js/jQuery.headroom.min.js"></script>
	<script src="${pageContext.servletContext.contextPath}/resources/assets/js/template.js"></script>
	<script src="${pageContext.servletContext.contextPath}/resources/js/datatable/js/jquery.dataTables.min.js"></script>
	<script src="${pageContext.servletContext.contextPath}/resources/js/datatable/js/dataTables.bootstrap.min.js"></script>
	
	<script src="${pageContext.servletContext.contextPath}/resources/js/moment.js"></script>
<script src="${pageContext.servletContext.contextPath}/resources/js/moment-timezone-with-data.js"></script>	
	
	
	
	<tiles:importAttribute name="currentPage" scope="request" />
	
	<!-- Fixed navbar -->
	<div class="navbar navbar-inverse navbar-fixed-top headroom" >
		<div class="container">
			<div class="navbar-header">
				<!-- Button for smallest screens -->
				<button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse"><span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span> </button>
				<a class="navbar-brand" href="${pageContext.servletContext.contextPath}"><img src="${pageContext.servletContext.contextPath}/resources/assets/images/logo_cp.png" alt="Conceptpower" width="40px"> <span class="first-letter">C</span>onceptpower</a>
			</div>
			<div class="navbar-collapse collapse">
				<ul class="nav navbar-nav pull-right">
					<tiles:insertAttribute name="navigation" />
				</ul>
			</div><!--/.nav-collapse -->
		</div>
	</div> 
	<!-- /.navbar -->

	<header id="head" class="secondary"></header>

	<!-- container -->
	<div class="container main">
		<ol class="breadcrumb">
			<tiles:importAttribute />
			<li><a href="${pageContext.servletContext.contextPath}">Home</a></li>
			<c:if test="${not empty prevURL}">
				<li><a
					href="${pageContext.servletContext.contextPath}/${prevURL}"><tiles:insertAttribute
							name="prevPage" /></a></li>
			</c:if>
			<li class="active"><tiles:insertAttribute name="currentPage" /></li>
		</ol>
		<sec:authorize access="isAuthenticated()">
			<p class="alignright">
				<i class="fa fa-user"></i>  <sec:authentication property="principal.username" /> 
			</p>
		</sec:authorize>
		
		<div class="row">
			
			<!-- Article main content -->
			<article class="col-sm-12 maincontent">
				<tiles:insertAttribute name="content" />
			</article>
			<!-- /Article -->

		</div>
	</div>	<!-- /container -->

	<footer id="footer" class="top-space sticky">

		

		<div class="footer2">
			<div class="container">
				<div class="row">
					
					<div class="col-md-6 widget">
						<div class="widget-body">
							<p class="simplenav">
								<c:set var="PR" value="${pullrequest}" />
			    				Version: ${buildNumber}<c:if test="${not empty PR}">, Pull Request: ${pullrequest}</c:if>
							</p>
						</div>
					</div>

					<div class="col-md-6 widget">
						<div class="widget-body">
							<p class="text-right">
								Designed by <a href="http://gettemplate.com/" rel="designer">gettemplate</a> 
							</p>
						</div>
					</div>

				</div> <!-- /row of widgets -->
			</div>
		</div>
	</footer>	
		
	
</body>
</html>