<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page session="false"%>

<script>
	$(function() {
		$("#addsynonym").click(function() {
			$("#dialog").dialog();
		});

	});

	$(document).ready(function() {
		$("#synonymstable").dataTable({
			"bJQueryUI" : true,
			"sPaginationType" : "full_numbers",
			"bAutoWidth" : false
		});

	});

	$(function() {
		$("#synonymsearch")
				.click(
						function() {
							var synonymname = $("#synonymname").val();
							$
									.ajax({
										type : "GET",
										url : "${pageContext.servletContext.contextPath}/synonymView",
										data : {
											synonymname : synonymname
										},
										success : function(response) {

											var html = '<table cellpadding="0" cellspacing="0" class="display dataTable" id="synonymstable"><thead><tr><th>Term</th><th>POS</th><th>Description</th></tr></thead><tbody><tbody>';
											var len = response.length;
											for (var i = 0; i < len; i++) {
												html += '<tr class="gradeX"><td align="justify"><font size="2">'
														+ response[i].word
														+ '</font></td>';
												html += '<td align="justify"><font size="2">'
														+ response[i].pos
														+ '</font></td>';
												html += '<td align="justify"><font size="2">'
														+ response[i].description
														+ '</font></td></tr>';
											}
											html += '</tbody></table>';
											$("#synonymViewDiv").html(html);

											$("#synonymstable")
													.dataTable(
															{
																"bJQueryUI" : true,
																"sPaginationType" : "full_numbers",
																"bAutoWidth" : false
															});

										}
									});
						});
	});

	$(document).ready(function() {
		$('#synonyms').dataTable({
			"bJQueryUI" : true,
			"sPaginationType" : "full_numbers",
			"bAutoWidth" : false
		});

	});
</script>

<h1>Add new concept</h1>
<p>Add a new concept here.</p>

<form
	action="${pageContext.servletContext.contextPath}/auth/concepts/createconcept"
	method='post'>
	<table>
		<tr>
			<td>Concept</td>
			<td><input type="text" name="name" id="name"></td>
		</tr>
		<tr>
			<td>POS</td>
			<td><select name="pos">
					<option value="noun">Nouns</option>
					<option value="verb">Verb</option>
					<option value="adverb">Adverb</option>
					<option value="adjective">Adjective</option>
					<option value="other">Other</option>
			</select></td>
		</tr>
		<tr>
			<td>Concept List</td>

			<td><form:select path="lists" name="lists">
					<form:option value="" label="Select concept list" />
					<form:options items="${lists}" />
				</form:select></td>

		</tr>
		<tr>
			<td>Description</td>
			<td><input type="text" name="description" id="description"></td>
		</tr>
		<tr>
			<td>Synonyms</td>
			<td></td>
			<td><input type="button" name="synonym" id="addsynonym"
				value="Add Synonym"></td>
		</tr>
		<tr>
			<td>Concept Type</td>
			<td><form:select path="types" name="types">
					<form:option value="" label="Select one" />
					<form:options items="${types}" />
				</form:select></td>
		</tr>
		<tr>
			<td>Equals</td>
			<td><input type="text" name="equals" id="equals"></td>
		</tr>
		<tr>
			<td>Similar to</td>
			<td><input type="text" name="similar" id="similar"></td>
		</tr>
		<tr>
			<td colspan="2"><input type="submit" value="Add Concept"></td>
		</tr>
	</table>

</form>
<form>
	<div id="dialog" title="Search synonym">

		<table>
			<tr>
				<td><input type="text" name="synonymname" id="synonymname"></td>
				<td><input type="button" name="synsearch" id="synonymsearch"
					value="Search"></td>
			</tr>
		</table>
		<div id="synonymViewDiv"
			style="max-width: 1000px; max-height: 500px; width: 100%;"></div>

	</div>
</form>

