<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">
<meta name="apple-mobile-web-app-capable" content="yes">
<title>Lucene Index</title>
<link type="text/css" rel="stylesheet"
	href="https://fonts.googleapis.com/css?family=Roboto:400,500">
<link type="text/css" rel="stylesheet"
	href="${pageContext.servletContext.contextPath}/resources/css/waitMe.css">

<h1>Index Management</h1>
</head>
<body>
	<br/><br/>
	<div id="page">
		<div id="page_content">
			<div class="containerBlock">
				<form:form
					action="${pageContext.servletContext.contextPath}/auth/indexLuceneWordNet"
					method='post' id="indexLucene">
					<div id="div1"></div>
					<table style="vertical-align: top">
						<tr>
							<td>
								<button type="button" class="btn btn-primary" id="waitMe_ex">Index
									WordNet Wrappers</button>
							</td>

							<td>
								<button type="button" class="btn btn-primary" id="waitMe_Delete">Delete
									Indexes</button>
							</td>
						</tr>
					</table>
					<br />
					<br />
					<br />
					<table style="vertical-align: top" border="1" width="400">
						<tr>
							<th align="center">Total Indexed Concepts</th>
							<th align="center">Last run</th>
						</tr>
						<tr>
							<td align="center" id="indexCount">${bean.indexedWordsCount}</td>
							<td align="center" id="lastRun">${bean.lastRun}</td>
						</tr>
					</table>

				</form:form>
			</div>

		</div>

	</div>

	<script src="http://cdn.jsdelivr.net/jquery/3.0.0-beta1/jquery.min.js"></script>
	<script
		src="${pageContext.servletContext.contextPath}/resources/js/waitMe.js"></script>

	<script>
		$(function() {

			var current_effect = $('#waitMe_ex_effect').val();
			$('#waitMe_Delete')
					.click(
							function() {
								run_waitMe(current_effect);
								$
										.ajax({
											url : "${pageContext.servletContext.contextPath}/auth/deleteConcepts",
											type : "POST",
											success : function(result) {
												var output = "<center><b>"
														+ result.message
														+ " </b> </center>";
												$("#div1").html(output);
												$('#indexCount')
														.html(
																result.indexedWordsCount);
												$('#lastRun').html(
														result.lastRun);
												waitMeClose();
											},
											error : function(result) {
												var output = "<font color=\"red\">"
														+ result.message
														+ "</font>";
												$("#div1").html(output);
												waitMeClose();
											}
										});
							});

			$('#waitMe_ex')
					.click(
							function() {
								run_waitMe(current_effect);
								$
										.ajax({
											url : "${pageContext.servletContext.contextPath}/auth/indexLuceneWordNet",
											type : "POST",
											success : function(result) {
												var output = "<center><b>"
														+ result.message
														+ " </b> </center>";
												$("#div1").html(output);
												$('#indexCount')
														.html(
																result.indexedWordsCount);
												$('#lastRun').html(
														result.lastRun);
												waitMeClose();
											},
											error : function(result) {
												var output = "<font color=\"red\">"
														+ result.message
														+ "</font>";
												$("#div1").html(output);
												waitMeClose();
											}
										});
							});

			function waitMeClose() {
				$('.containerBlock > form').waitMe('hide');
			}

			function run_waitMe(effect) {
				$('.containerBlock > form').waitMe({
					effect : effect,
					text : 'Please wait...',
					bg : 'rgba(255,255,255,0.7)',
					color : '#000',
					maxSize : '',
					source : 'img.svg',
					onClose : function() {
					}
				});
			}
		});
	</script>
</body>

