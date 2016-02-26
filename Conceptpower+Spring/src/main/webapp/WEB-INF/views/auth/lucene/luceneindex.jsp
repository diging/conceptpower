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
<title>waitMe</title>
<link type="text/css" rel="stylesheet"
	href="https://fonts.googleapis.com/css?family=Roboto:400,500">
<link type="text/css" rel="stylesheet"
	href="${pageContext.servletContext.contextPath}/resources/css/waitMe.css">

<style>
/* btn */
.btn {
	display: inline-block;
	cursor: pointer;
	background: #fff;
	border: 1px solid #bbb;
	height: 34px;
	padding: 6px 12px;
	font-size: 14px;
	line-height: 18px
}

.btn.btn-default {
	
}

.btn.btn-default:hover {
	background: #eee;
	border-color: #bbb
}

.btn.btn-default:focus {
	background: #ddd;
	border-color: #bbb
}

.btn.btn-primary {
	background-color: #007ec4;
	border-color: #005A8C;
	color: #fff
}

.btn.btn-primary:hover {
	background-color: #158CCF;
	border-color: #005A8C
}

.btn.btn-primary:focus {
	background-color: #005A8C;
	border-color: #005A8C
}

.btn.btn-default[disabled] {
	background: #fafafa !important;
	border-color: #ccc !important;
	color: #aaa
}

.btn.btn-primary[disabled] {
	background: #3F9DD0 !important;
	border-color: #537FA9 !important;
	color: #ACD3E8;
	box-shadow: none !important
}

.btn.btn-left {
	float: left;
	margin: 0 5px 0 0 !important
}
</style>

</head>
<body>

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
					<table style="vertical-align: top" border="1" width="350">
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

