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
<script type="text/javascript">
	$(document).ready(function() {
		var lastRun = $('#lastRun').val();
		var d = new Date(lastRun);
		setDateTime(d.toLocaleString());
	});
	function setDateTime(lastRun) {
		$("#luceneTable tr td:last-child").html(lastRun);
	};
</script>
</head>
<body>
	<br/><br/>
	<div id="page">
		<div id="page_content">
			<div class="containerBlock">
				<form:form
					action="${pageContext.servletContext.contextPath}/auth/indexConcepts"
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
					<table id="luceneTable" style="vertical-align: top" border="1" width="400">
						<tr>
							<th align="center">Total Indexed Concepts</th>
							<th align="center">Last run</th>
						</tr>
						<tr>
							<td align="center" id="indexCount">${bean.indexedWordsCount}</td>
							<td align="center" id="lastRun-1">${bean.lastRun}</td>
						</tr>
					</table>
				<input type="hidden" id="lastRun" value="${bean.lastRun}" />
				</form:form>
			</div>

		</div>

	</div>

	<script
		src="${pageContext.servletContext.contextPath}/resources/js/waitMe.js"></script>

	<script>
		$(function() {

			var current_effect = $('#waitMe_ex_effect').val();
			$('#waitMe_Delete')
					.click(
							function() {
								run_waitMe(current_effect);
								return $.ajax({
											url : "${pageContext.servletContext.contextPath}/auth/deleteIndex",
											type : "POST",
											timeout : 3600000,
											success : function(result) {
												setIndexValues(result);
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
							function index() {
								run_waitMe(current_effect);
								$.ajax({
											url : "${pageContext.servletContext.contextPath}/auth/indexConcepts",
											type : "POST",
											success: function(result) {
											    // Poll here
											    checkIndexerStatus();
										        
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
			
			function checkIndexerStatus(){
			    $.ajax({
					url : "${pageContext.servletContext.contextPath}/auth/getIndexerStatus",
					type : "POST",
					success: function(result) {
					    // Poll here
					    console.log(result);
					    if(result.message == 'Indexer Running') {
					        setTimeout(checkIndexerStatus,5000);    
					    }
					}
				});
			}

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
			
			function setIndexValues(result) {
				var output = "<center><b>" + result.message + " </b> </center>";
				$("#div1").html(output);
				$('#indexCount')
						.html(result.indexedWordsCount);
				$('#lastRun-1')
						.html(result.lastRun);
				waitMeClose();
			}
			
		});
	</script>
</body>

