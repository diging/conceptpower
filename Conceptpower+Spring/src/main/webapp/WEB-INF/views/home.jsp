<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec"
  uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page session="false"%>
<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/>
<script type="text/javascript">
//# sourceURL=details.js
$(document).ready(function() {
  function removeColumnFromTable(str) {
    var target = $('table').find('th[data-name="' + str +'"]');
    var index = (target).index();
     $('table tr').find('th:eq(' + index + '),td:eq(' + index + ')' ).hide();
  }
  function showColumnFromTable(str) {
    var target = $('table').find('th[data-name="' + str +'"]');
    var index = (target).index();
     $('table tr').find('th:eq(' + index + '),td:eq(' + index + ')' ).show();
  }
  function checkCheckBoxAndRemoveColumnsForMergeConcepts() {
    var conceptsToBeMerged = $('#conceptIdsToMerge').val().replace("\"", "");
    if(conceptsToBeMerged) {
      removeColumnFromTable('delete');    
      removeColumnFromTable('edit');    
      $("#mergeConcept").show();
      $("#prepareMergeConcept").hide();
      // Check the checkboxes
      var conceptIds = conceptsToBeMerged.split(',');
      for(var i = 0; i<conceptIds.length; i++){
        $("input[value='" + conceptIds[i] + "']").prop('checked', true);
      }
      $('#conceptIdsToMerge').val(conceptsToBeMerged);
    } else {
      removeColumnFromTable('merge');    
      $("#mergeConcept").hide();
      $("#prepareMergeConcept").show();
    }
  }
  checkCheckBoxAndRemoveColumnsForMergeConcepts();
	$('#viafSearchResult').dataTable({
		"bJQueryUI" : true,
		"sPaginationType" : "full_numbers",
		"bAutoWidth" : false,
		"aoColumnDefs" : [ {
			"aTargets" : [ 2 ],
			"sType" : "html",
			"fnRender" : function(o, val) {
				return $("<div/>").html(o.aData[2]).text();
			}
		 } ],
	});
	$('#detailsModal')
		.on(
			'show.bs.modal',
			function(event) {
				var button = $(event.relatedTarget); // ` that triggered the modal
				var conceptid = button.data('conceptid'); // Extract info from data-* attributes
				// If necessary, you could initiate an AJAX request here (and then do the updating in a callback).
				// Update the modal's content. We'll use jQuery here, but you could use a data binding library or other methods instead.
				$.ajax({
					type : "GET",
					url : "${pageContext.servletContext.contextPath}/conceptDetail",
					data : {
						conceptid : conceptid
					},
					success : function(details) {
						details = $.parseJSON(details);
						$("#conceptTerm").text(details.name);
						$("#detailsid").text(details.id);
						$("#detailsuri").text(details.uri);
						$("#detailswordnetid").text(details.wordnetid);
						$("#detailspos").text(details.pos);
						$("#detailsconceptlist").text(details.conceptlist);
						$("#detailstypeid").text(details.type);
						$("#detailsequalto").text(details.equalto);
						$("#detailssimilarto").text(details.similarto);
						$("#detailscreator").text(details.creator);
						$("#detailsdescription").html(details.description);
						$("#detailsmergedIds").html(details.mergedIds);
						
            if(details.mergedIds) {
              $.ajax({
                url: '${pageContext.servletContext.contextPath}/rest/OriginalConcepts',
                type: 'GET',
                headers: {          
                    Accept: "application/json",         
                },
                data: {
                  ids: details.mergedIds
                },
              })
              .success(function(originalConcepts) {
                var mergedIdsHtml = '';
                $.each(originalConcepts.conceptEntries, function (index, conceptEntry) {
                    if(mergedIdsHtml) {
                      mergedIdsHtml += ", ";
                    }
                    mergedIdsHtml += '<span title =  "Id: '+ conceptEntry.id +', Word: '+ conceptEntry.lemma + ', Description: ' + conceptEntry.description + '">';
                    mergedIdsHtml += "<i>" + conceptEntry.id  + "</i>";
                    mergedIdsHtml += "</span>";
                });
                $("#detailsmergedIds").html(mergedIdsHtml);  
              });              
            }
                    }
                });
        });
    $('#prepareMergeConcept').on('click', function (e) {
        e.preventDefault();
        // Get the column API object
        removeColumnFromTable('edit');
        removeColumnFromTable('delete');
        showColumnFromTable('merge');
        $("#mergeConcept").show();
        $("#prepareMergeConcept").hide();
    });
    $(':checkbox').change(function(e) {
        if(this.checked) {
          mergeConcepts($(this).val())
        } else {
          removeMergedConcepts($(this).val());
        }
    });
    
    function mergeConcepts(conceptId) {
      $('#mergeError').hide();
      var conceptsToMerge = $('#conceptIdsToMerge').val().replace("\"", "");
      if(conceptsToMerge) {
        conceptsToMerge = conceptsToMerge + ',' + conceptId;
      } else {
        conceptsToMerge = conceptId;
      }
      $('#conceptIdsToMerge').val(conceptsToMerge);  
    }    
    function removeMergedConcepts(conceptId) {
        var conceptsToMerge = $('#conceptIdsToMerge').val().replace("\"", "");
        var conceptIds = conceptsToMerge.split(',');
        var updatedConceptIds = '';
        for(var i=0; i<conceptIds.length; i++) {
          if(conceptIds[i] != conceptId) {
            updatedConceptIds = updatedConceptIds + conceptIds[i] + ",";
          }
        }
        if(updatedConceptIds != '') {
          $('#conceptIdsToMerge').val(updatedConceptIds.substring(0, updatedConceptIds.length - 1));      
        } else { 
          $('#conceptIdsToMerge').val(updatedConceptIds);      
        }
    }
    $('#mergeError').hide();
});
                    
$(document).ready(hideFormProcessing);
function hideFormProcessing() {
    $('#floatingCirclesG').hide();
}
function showFormProcessing() {
    $('#floatingCirclesG').show();
}
function finalizeMerge() {
  var conceptIdsToMerge = $('#conceptIdsToMerge').val().replace("\"", "");
  var conceptIdsToBeMerged = conceptIdsToMerge.split(',');
  if(conceptIdsToBeMerged.length < 2) {
      $('#mergeError').show();
    } else {
        window.location = '${pageContext.servletContext.contextPath}/auth/concepts/merge?mergeIds=' + conceptIdsToMerge;      
    }
}
function paginate(page, sortDir, sortColumn, word, pos) {
      var conceptIdsToMerge = $('#conceptIdsToMerge').val().replace("\"", "");
      if(conceptIdsToMerge) {
        window.location = '${pageContext.servletContext.contextPath}/home/conceptsearch?page=' + page + '&sortDir=' + sortDir + '&sortColumn=' + sortColumn + '&word=' + word + '&pos=' + pos + '&conceptIdsToMerge=' + conceptIdsToMerge;  
      } else {
        window.location = '${pageContext.servletContext.contextPath}/home/conceptsearch?page=' + page + '&sortDir=' + sortDir + '&sortColumn=' + sortColumn + '&word=' + word + '&pos=' + pos;  
      }
      
    }
var conceptsToMerge = "";
function prepareMergeConcept(conceptId) {
    var tis = $(this);
    var conceptsToMerge = $(this).attr("value");
    if(conceptsToMerge != '') {
    	conceptsToMerge = conceptId;
    } else {
        conceptsToMerge = conceptsToMerge + "," + conceptId;    
    }
}
function createWrapper(wrapperId) {
  window.location = '${pageContext.servletContext.contextPath}/auth/conceptlist/addconceptwrapper?wrapperId=' + wrapperId;
}

$(document).ready(function(){
    $(document).on("click", ".fa-exclamation-triangle", function() {
		$("#fetchRequests").val($(this).data("request"));
    	$("#conceptId").val($(this).data("concept-id"));
    	$('#resolveCommentError').hide();
		$("#requestBox").show();    
	});
    
    $(document).on("click", ".fa-comment", function() {
    	$('#reviewError').hide();
    	$("#conceptId").val($(this).data("concept-id"));
    });
    
    $('#alertMsg').hide();
    $('#submitForm').click(function(e) {
        var request = $('#request').val().trim();
        var conceptId = $('#conceptId').val();
        
        if(request.length == 0){
        	$('#reviewError').show();
        }else {
	 	  	$.ajax({
	            type: "POST",
	            url: "${pageContext.servletContext.contextPath}/auth/request/add?${_csrf.parameterName}=${_csrf.token}",
	            data: "request=" + request + "&conceptId=" + conceptId + "&status=OPENED",
	            success: function(response){
	                  $('#request').val('');
	                  $('#myModal').modal('hide');
	            },
	            error: function(e){
	                  $('#alertMsg').html('<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Following error occurred in posting the request :'+e);
	                  $('#alertMsg').show();
	            }
	        });
	        var request_subString = request.substring(0,79);
	        //Escaping Single quotes
	        conceptId = conceptId.replace(/'/g, "\\'");
	        var commentTd = $("#comment-" + conceptId);
	       	commentTd.html('<div  data-request="'+request+'"  title="'+request_subString+'"  data-toggle="modal" data-target="#requestModal" data-concept-id="'+conceptId+'" style="color:#19586B" class="fa fa-exclamation-triangle"></div>');
	       	$("#fetchRequests").val(request);
        }
    });
    
    $('#resolveButton').click(function(){	
		var resolveComment = $('#resolveComment').val().trim();
		var conceptId = $("#conceptId").val();
		var request = $("#fetchRequests").val();
	
		if(resolveComment.length == 0){
			$('#resolveCommentError').text('Comment cannot be empty');
			$('#resolveCommentError').show();
		}else{
			$.ajax({
				type:"POST",
				url:"${pageContext.servletContext.contextPath}/auth/request/resolve?${_csrf.parameterName}=${_csrf.token}",
				data: "resolvingComment=" + resolveComment + "&conceptId="+conceptId + "&status=RESOLVED",
				success: function(response){
					$('#requestModal').modal('hide');
			    	$('#resolveComment').val('');
					conceptId = conceptId.replace(/'/g, "\\'");
			        var commentTd = $("#comment-" + conceptId);
			        
					commentTd.html('<div data-concept-id="'+conceptId+'" title="Add a review request" data-toggle="modal" data-target="#myModal" class="fa fa-comment" style="color:#19586B"></div>'
								  +'<div data-concept-id="'+conceptId+'" data-request="'+request+'" data-resolve-comment="'+resolveComment+'" title="Reopen Review Request" data-toggle="modal" data-target="#reopenModal" class="fa fa-refresh" style="color:#19586B"></div>');
				},
				error: function(e){
					$('#resolveCommentError').text("Something went wrong. Unable to resolve the request");
				}
			});
		}
	});
	
	$(document).on("click",".fa-refresh", function(){
		$("#conceptId").val($(this).data("concept-id"));
		$("#fetchReopenRequests").val($(this).data('request'));
		$("#reopenComment").val($(this).data('resolveComment')); 
		
		var conceptId = $("#conceptId").val().replace(/'/g, "\\'");
        var reviewHistoryId = $("#review-history-" + conceptId);
        $(reviewHistoryId).show();
	});
	
	$("#reopenModal").on("hide.bs.modal", function(e){
		var conceptId = $("#conceptId").val().replace(/'/g, "\\'");
        var reviewHistoryId = $("#review-history-" + conceptId);
        $(reviewHistoryId).hide();
	});
	
	$("#reopenButton").click(function(){
		var operationToPerform = $("#reopenButton").val();
		var conceptId = $("#conceptId").val();
		var request = $("#fetchReopenRequests").val();
		
		$.ajax({
			type:"POST",
			url:"${pageContext.servletContext.contextPath}/auth/request/reopen?${_csrf.parameterName}=${_csrf.token}",
			data: "&conceptId="+conceptId + "&status=OPENED",
			success: function(response){
				$('#reopenModal').modal('hide');
				var request_subString = request.substring(0,79);
				
				conceptId = conceptId.replace(/'/g, "\\'");
				var commentTd = $("#comment-" + conceptId);
				commentTd.html('<div  data-request="'+request+'"  title="'+request_subString+'"  data-toggle="modal" data-target="#requestModal" data-concept-id="'+conceptId+'" style="color:#19586B" class="fa fa-exclamation-triangle"></div>');
			},
			error: function(e){
				$('#reopenError').text("Something went wrong. Unable to reopen the request");
			}
		});
	});

});
</script>

<header class="page-header">
   <h1 class="page-title">Welcome to Conceptpower</h1>
</header>
<p>Search for a concept:</p>
<center>
   <font color="red">${IndexerStatus}</font>
</center>

<form:form
  action="${pageContext.servletContext.contextPath}/home/conceptsearch"
  method='get' modelAttribute='conceptSearchBean'>
  <form:errors path="luceneError"></form:errors>
  <input type='hidden' id='conceptIdsToMerge' value='${conceptIdsToMerge}' />
    <div id="mergeError" class="alert alert-danger">
      Please select at least two concepts to merge.
   </div>
   
   <div class="row">
      <div class="col-sm-8">
         <form:input path="word" placeholder="Enter search term"
            class="form-control" />
      </div>
      <div class="col-sm-2">
         <form:select path="pos" name="pos" class="form-control">
            <form:options items="${conceptSearchBean.posMap}" />
         </form:select>
      </div>
      <div class="col-sm-2">
         <input type="submit" value="Search" class="btn btn-action"
            onclick="showFormProcessing()" onsubmit="hideFormProcessing()">
      </div>
   </div>
   
   <div class="row">
      <div class="col-sm-12">
         <form:errors path="foundConcepts" class="ui-state-error-text"></form:errors>
      </div>
   </div>
   
   <div class="row" style="margin-top: 15px;">
      <div class="col-sm-12">
         <div id="floatingCirclesG">
            <div class="f_circleG" id="frotateG_01"></div>
            <div class="f_circleG" id="frotateG_02"></div>
            <div class="f_circleG" id="frotateG_03"></div>
            <div class="f_circleG" id="frotateG_04"></div>
            <div class="f_circleG" id="frotateG_05"></div>
            <div class="f_circleG" id="frotateG_06"></div>
            <div class="f_circleG" id="frotateG_07"></div>
            <div class="f_circleG" id="frotateG_08"></div>
         </div>
      </div>
   </div>
</form:form>

<hr>
<c:if test="${not empty conceptSearchBean.foundConcepts}">
   <h3>Results</h3>

   <sec:authorize access="isAuthenticated()">
      <div class="row">
         <div class="col-sm-2">
            <button id="prepareMergeConcept" class="btn-sm btn-action" style="margin-bottom: 15px;">Select concepts to merge</button>
            <button id="mergeConcept" class="btn-sm btn-action" style="margin-bottom: 15px;" onclick="finalizeMerge();">Merge selected concepts</button>
         </div>
      </div>
   </sec:authorize>
   
   <c:choose>
      <c:when test="${sortDir == -1}">
         <c:set var="sortDirection" value="${1}"/>
      </c:when>
      <c:otherwise>
         <c:set var="sortDirection" value="${-1}"/>
      </c:otherwise>
   </c:choose>

<style>
      .fa-exclamation-triangle {
      position: relative;
      display: inline-block;
      }
   </style>
   <table
      class="table table-striped table-bordered" id="conceptSearchResult">
      <thead>
         <tr>
            <sec:authorize access="isAuthenticated()">
               <th data-name='edit'></th>
               <th data-name='delete'></th>
               <th data-name='merge'></th>
            </sec:authorize>
            <th>
               <a href="#" onclick="paginate('${page}', '${sortDirection}', 'word', '${conceptSearchBean.word}', '${conceptSearchBean.pos}');" >
                  Term  
                  <c:choose>
                     <c:when test="${sortColumn == 'word' && sortDir == 1}"><i class="fa fa-sort-desc"></i></c:when>
                     <c:when test="${sortColumn == 'word' && sortDir == -1}"><i class="fa fa-sort-asc"></i></c:when>
                     <c:otherwise><i class="fa fa-sort"></i></c:otherwise>
                  </c:choose>
               </a>
            </th>
            <th>
               <a href="#" onclick="paginate('${page}', '${sortDirection}', 'id', '${conceptSearchBean.word}', '${conceptSearchBean.pos}');" >
                  ID  
                  <c:choose>
                     <c:when test="${sortColumn == 'id' && sortDir == 1}"><i class="fa fa-sort-desc"></i></c:when>
                     <c:when test="${sortColumn == 'id' && sortDir == -1}"><i class="fa fa-sort-asc"></i></c:when>
                     <c:otherwise><i class="fa fa-sort"></i></c:otherwise>
                  </c:choose>
               </a>
            </th>
            <th>
               <a href="#" onclick="paginate('${page}', '${sortDirection}', 'wordnetid', '${conceptSearchBean.word}', '${conceptSearchBean.pos}');" >
                  Wordnet ID  
                  <c:choose>
                     <c:when test="${sortColumn == 'wordnetid' && sortDir == 1}"><i class="fa fa-sort-desc"></i></c:when>
                     <c:when test="${sortColumn == 'wordnetid' && sortDir == -1}"><i class="fa fa-sort-asc"></i></c:when>
                     <c:otherwise><i class="fa fa-sort"></i></c:otherwise>
                  </c:choose>
               </a>
            </th>
            <th>
               <a href="#" onclick="paginate('${page}', '${sortDirection}', 'pos', '${conceptSearchBean.word}', '${conceptSearchBean.pos}');" >
                  POS  
                  <c:choose>
                     <c:when test="${sortColumn == 'pos' && sortDir == 1}"><i class="fa fa-sort-desc"></i></c:when>
                     <c:when test="${sortColumn == 'pos' && sortDir == -1}"><i class="fa fa-sort-asc"></i></c:when>
                     <c:otherwise><i class="fa fa-sort"></i></c:otherwise>
                  </c:choose>
               </a>
            </th>
            <th>
               <a href="#" onclick="paginate('${page}', '${sortDirection}', 'listName', '${conceptSearchBean.word}', '${conceptSearchBean.pos}');" >
                  Concept List  
                  <c:choose>
                     <c:when test="${sortColumn == 'listName' && sortDir == 1}"><i class="fa fa-sort-desc"></i></c:when>
                     <c:when test="${sortColumn == 'listName' && sortDir == -1}"><i class="fa fa-sort-asc"></i></c:when>
                     <c:otherwise><i class="fa fa-sort"></i></c:otherwise>
                  </c:choose>
               </a>
            </th>
            <th>
               <a href="#" onclick="paginate('${page}', '${sortDirection}', 'description', '${conceptSearchBean.word}', '${conceptSearchBean.pos}');" >
                  Description  
                  <c:choose>
                     <c:when test="${sortColumn == 'description' && sortDir == 1}"><i class="fa fa-sort-desc"></i></c:when>
                     <c:when test="${sortColumn == 'description' && sortDir == -1}"><i class="fa fa-sort-asc"></i></c:when>
                     <c:otherwise><i class="fa fa-sort"></i></c:otherwise>
                  </c:choose>
               </a>
            </th>
            <th>
               <a href="#" onclick="paginate('${page}', '${sortDirection}', 'types', '${conceptSearchBean.word}', '${conceptSearchBean.pos}');" >
                  Type  
                  <c:choose>
                     <c:when test="${sortColumn == 'types' && sortDir == 1}"><i class="fa fa-sort-desc"></i></c:when>
                     <c:when test="${sortColumn == 'types' && sortDir == -1}"><i class="fa fa-sort-asc"></i></c:when>
                     <c:otherwise><i class="fa fa-sort"></i></c:otherwise>
                  </c:choose>
               </a>
            </th>
            <th>
               <div style="color:#19586B">Review Status</div>
            </th>
         </tr>
      </thead>
      <tbody>
         <c:forEach var="concept"
            items="${conceptSearchBean.foundConcepts}">
            <tr class="gradeX" title="${concept.uri}">
               <sec:authorize access="isAuthenticated()">
                  <td>
                     <c:choose>
                        <c:when
                           test="${not fn:containsIgnoreCase(concept.entry.id, 'WID')}">
                           <a
                              href="${pageContext.servletContext.contextPath}/auth/conceptlist/editconcept/${concept.entry.id}?fromHomeScreen=true">
                           <i title="Edit Concept"
                              class="fa fa-pencil-square-o"></i>
                           </a>
                        </c:when>
                        <c:otherwise>
                           <a href="#" onclick="createWrapper('${concept.entry.id}');">
                           <i title='Create wrapper' class="fa fa-pencil-square-o"></i>
                           </a>
                        </c:otherwise>
                     </c:choose>
                  </td>
                  <td>
                     <c:choose>
                        <c:when
                           test="${not fn:containsIgnoreCase(concept.entry.id, 'WID')}">
                           <a
                              href="${pageContext.servletContext.contextPath}/auth/conceptlist/deleteconcept/${concept.entry.id}?fromHomeScreenDelete=true"
                              id="${concept.entry.id}"><i
                              title="Delete Concept" class="fa fa-trash"></i></a>
                        </c:when>
                        <c:otherwise>
                           <i title="Cannot delete WordNet concepts"
                              class="fa fa-trash disabled"></i>
                        </c:otherwise>
                     </c:choose>
                  </td>
                  <td>
                     <input type="checkbox" id="conceptMergeCheckbox" name="conceptMergeCheckbox" value="${concept.entry.id }" />
                  </td>
               </sec:authorize>
               <td align="justify">
                  <a
                     id="${concept.entry.id}" data-toggle="modal"
                     data-target="#detailsModal"
                     data-conceptid="${concept.entry.id}" >
                     <c:out
                        value="${concept.entry.word}"></c:out>
                  </a>
               </td>
               <td class="entryID" align="justify" >
                  <c:out
                     value="${concept.entry.id}"></c:out>
               </td>
               <td align="justify">
                  <c:out
                     value="${concept.entry.wordnetId}"></c:out>
               </td>
               <td align="justify">
                  <c:out
                     value="${concept.entry.pos}"></c:out>
               </td>
               <td align="justify">
                  <c:out
                     value="${concept.entry.conceptList}"></c:out>
               </td>
               <td align="justify">
                  <div class="scrollable" style="max-height: 100px; max-width: 400px;">
                     <c:out value="${concept.description}" escapeXml="false"></c:out>
                  </div>
               </td>
               <td align="justify">
                  <c:out
                     value="${concept.type.typeName}"></c:out>
               </td>

               <!-- Enabling Disabling the Review button -->  
               <c:choose>
                  <c:when 
                     test="${concept.reviewRequest.size() <= 0 || concept.reviewRequest.get(concept.reviewRequest.size()-1).status == 'RESOLVED'}">
                     <!-- Testing if the request has already been provided. -->
                     <td align="center" id="comment-${concept.entry.id}"  >
                        <div data-concept-id="${concept.entry.id}" title="Add a review request"  data-toggle="modal" data-target="#myModal" class="fa fa-comment" style="color:#19586B"></div>
                        <c:if
                        test="${concept.reviewRequest.size() > 0  && concept.reviewRequest.get(concept.reviewRequest.size()-1).status == 'RESOLVED'}">
                        <div data-concept-id="${concept.entry.id}" data-request="${concept.reviewRequest.get(concept.reviewRequest.size()-1).request}" data-resolve-comment="${concept.reviewRequest.get(concept.reviewRequest.size()-1).resolvingComment.get(concept.reviewRequest.get(concept.reviewRequest.size()-1).resolvingComment.size()-1)}" title="Reopen Review Request" data-toggle="modal" data-target="#reopenModal" class="fa fa-refresh" style="color:#19586B"></div>
                        </c:if>
                     </td>
                  </c:when>
                 
                  <c:otherwise>
                     <td align="center" id="comment-${concept.entry.id}" >
                        <div  data-request="${concept.reviewRequest.get(concept.reviewRequest.size()-1).request}" title="${fn:substring(concept.reviewRequest.get(concept.reviewRequest.size()-1).request,0,79)}"  class="fa fa-exclamation-triangle" data-toggle="modal" data-target="#requestModal" data-concept-id="${concept.entry.id}" style="color:#19586B" id="exclamation" >
                        </div>
                     </td>
                  </c:otherwise>
               </c:choose>
            </tr>
         </c:forEach>
      </tbody>
   </table>
   <nav aria-label="Page navigation">
      <ul class="pagination">
         <li 
         <c:if test="${page == 1}">class="disabled"</c:if>
         >
         <a 
         <c:if test="${page > 1}"> href="#" onclick="paginate('${page - 1}', '${sortDir}', '${sortColumn}', '${conceptSearchBean.word}', '${conceptSearchBean.pos}');"</c:if>
         aria-label="Previous" >
         <span aria-hidden="true">&laquo;</span>
         </a>
         </li>
         <c:forEach begin="1" end="${count}" var="val">
            <li 
            <c:if test="${val == page}">class="active"</c:if>
            >
            <a href="#" onclick="paginate('${val}', '${sortDir}', '${sortColumn}', '${conceptSearchBean.word}', '${conceptSearchBean.pos}');" >
               <c:out value="${val}"/>
            </a>
            </li>
         </c:forEach>
         <li 
         <c:if test="${page == count}">class="disabled"</c:if>
         >
         <a 
         <c:if test="${page < count}"> href="#" onclick="paginate('${page + 1}', '${sortDir}', '${sortColumn}', '${conceptSearchBean.word}', '${conceptSearchBean.pos}');"</c:if>
         aria-label="Next">
         <span aria-hidden="true">&raquo;</span>
         </a>
         </li>
      </ul>
   </nav>
</c:if>
<div class="modal fade" id="detailsModal" tabindex="-1" role="dialog"
   aria-labelledby="mySmallModalLabel">
   <div class="modal-dialog modal-md">
      <div class="modal-content">
         <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal"
               aria-label="Close">
            <span aria-hidden="true">&times;</span>
            </button>
            <h4 class="modal-title" id="myModalLabel">
               Concept Details: <i id="conceptTerm"></i>
            </h4>
         </div>
         <div class="modal-body">
            <div class="row row-odd">
               <div class="col-sm-3">Id:</div>
               <div id="detailsid" class="col-sm-9"></div>
         
            </div>
            <div class="row row-even">
               <div class="col-sm-3">URI:</div>
               <div id="detailsuri" class="col-sm-9"></div>
            </div>
            <div class="row row-odd">
               <div class="col-sm-3">Wordnet Id:</div>
               <div id="detailswordnetid" class="col-sm-9"></div>
            </div>
            <div class="row row-even">
               <div class="col-sm-3">Description:</div>
               <div id="detailsdescription" class="col-sm-9"></div>
            </div>
            <div class="row row-odd">
               <div class="col-sm-3">POS:</div>
               <div id="detailspos" class="col-sm-9"></div>
            </div>
            <div class="row row-even">
               <div class="col-sm-3">Concept List:</div>
               <div id="detailsconceptlist" class="col-sm-9"></div>
            </div>
            <div class="row row-odd">
               <div class="col-sm-3">Type:</div>
               <div id="detailstypeid" class="col-sm-9"></div>
            </div>
            <div class="row row-even">
               <div class="col-sm-3">Equal to:</div>
               <div id="detailsequalto" class="col-sm-9"></div>
            </div>
            <div class="row row-odd">
               <div class="col-sm-3">Similar to:</div>
               <div id="detailssimilarto" class="col-sm-9"></div>
            </div>
            <div class="row row-even">
               <div class="col-sm-3">Creator:</div>
               <div id="detailscreator" class="col-sm-9"></div>
            </div>
            <div class="row row-odd">
               <div class="col-sm-3">Merged Ids:</div>
               <div id="detailsmergedIds" class="col-sm-9"></div>
            </div>
         </div>
      </div>
   </div>
</div>
<form id="reviewForm" action="${pageContext.servletContext.contextPath}/auth/request/add" method="post">
   <div class="modal" tabindex="-1" id="myModal" role="dialog">
      <div class="modal-dialog" role="document">
         <!-- Modal content-->
         <div class="modal-content">
            <div class="modal-header">
               <button  type="button" class="close" data-dismiss="modal" aria-label="Close">
               <span aria-hidden="true">&times;</span>
               </button>
               <h5 class="modal-title">Request for Review</h5>
            </div>
            <div class="modal-body">
               <div class="form-label"><b>Request</b></div>
               <div class="form-field">
                  <textarea class="form-control" id="request" name="request" rows="4" cols="30" placeholder="Please enter a request." ></textarea>
                  <input type="hidden" name="conceptId" id="conceptId" value=""/>
                  <div id="reviewError" class="error">Request cannot be empty</div>
                  <div id="info" class="success"></div>
               </div>
               <div class="modal-footer">
                  <input type="button" class="btn btn-primary" style="color:white;background:#FF9B22" value = "Close" data-dismiss="modal">
                  <input value="Submit Form" type="button" id="submitForm" class="btn btn-primary" style="color:white;background:#FF9B22" >
               </div>
            </div>
         </div>
      </div>
   </div>
</form>
<!-- Modal -->
<div class="modal fade" id="requestModal" role="dialog" >
   <div class="modal-dialog" id="requestBox">
      <form>
         <!-- Modal content-->
         <div class="modal-content" >
            <div class="modal-header">
               <button  type="button" class="close" data-dismiss="modal" aria-label="Close">
               <span aria-hidden="true">&times;</span>
               </button>
               <h5 class="modal-title">Resolve Request</h5>
            </div>
            <div class="modal-body">
               <div class="form-field">
                  <div class="floatingform" >
                     <div>
                        <div class="form-field">
                           <textarea disabled class="form-control" style="border: none" id="fetchRequests" name="fetchRequests" rows="4" ></textarea>
                        </div>
                     </div>
                  </div>
               </div>
               <div id = "resolveArea" style="padding-top: 25px; display: inline-flex;">
	               	<textarea class="form-control" id="resolveComment" name="resolveComment" rows="1" cols="60" placeholder="Please enter a closing Comment"></textarea>
	               	<input type="button" id="resolveButton" class="btn btn-primary" style="color:white;background:#FF9B22;margin-left:20px" value = "RESOLVE">
               </div>
               <div id="resolveCommentError" class="error"></div>
            </div>
         </div>
      </form>
   </div>
</div>
<div class="alert alert-danger alert-dismissible" id="alertMsg"></div>

<!--Reopen Modal-->
<div class="modal fade" id="reopenModal" role="dialog" >
   <div class="modal-dialog" id="requestBox">
      <form>
         <!-- Modal content-->
         <div class="modal-content" >
            <div class="modal-header">
               <button  type="button" class="close" data-dismiss="modal" aria-label="Close">
               <span aria-hidden="true">&times;</span>
               </button>
               <h5 class="modal-title">Reopen Request</h5>
            </div>
            <div class="modal-body">
               <div class="form-field">
                  <div class="floatingform" >
                     <div>
                        <div class="form-field">
                           <textarea disabled class="form-control" style="border: none" id="fetchReopenRequests" name="fetchRequests" rows="4" ></textarea>
                        </div>
                     </div>
                  </div>
               </div>
               
               <c:forEach var="concept"
            	items="${conceptSearchBean.foundConcepts}">
            	<ul id="review-history-${concept.entry.id}" hidden=true>
            		
            		<c:forEach var="review" items="${concept.reviewRequest}" >
            		<li class="list-group-item">${review.request}</li>
            			<ul>
            				<c:forEach var="comment" items="${review.resolvingComment}">
            				<li class="list-group-item">${comment}</li>
            				</c:forEach>
            			</ul>
            		</c:forEach>
            		
            	 </ul>
               </c:forEach>
               <div id = "resolveArea" style="padding-top: 25px; display: inline-flex;">
	               	<textarea disabled class="form-control" id="reopenComment" name="reopenComment" rows="1" cols="60" placeholder="Please enter a closing Comment"></textarea>
	               	<input type="button" id="reopenButton" class="btn btn-primary" style="color:white;background:#FF9B22;margin-left:20px" value = "REOPEN">
               </div>
               <div id="reopenError" class="error"></div>
            </div>
         </div>
      </form>
   </div>
</div>