<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec"
  uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page session="false"%>

<script type="text/javascript">
//# sourceURL=details.js
$(document).ready(function() {

  function removeColumnFromTable(str) {
    var target = $('table').find('th[data-name="' + str +'"]');
    var index = (target).index();
     $('table tr').find('th:eq(' + index + '),td:eq(' + index + ')' ).remove();
  }

  function checkCheckBoxForMergeConcepts() {
    var conceptsToBeMerged = $('#conceptIdsToMerge').val();
    console.log(conceptsToBeMerged);
  }

  removeColumnFromTable('delete');
  checkCheckBoxForMergeConcepts();

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
					}
				});
		});

    $('#prepareMergeConcept').on('click', function (e) {
        e.preventDefault();
        // Get the column API object
        removeColumnFromTable('edit');
        removeColumnFromTable('delete');
        $("#mergeConcept").show('merge', 'conceptSearchResult');
        $("#prepareMergeConcept").hide();
    });

    $(':checkbox').change(function(e) {
        if(this.checked) {
          console.log($(this).val());
          mergeConcepts($(this).val())
        } else {
          console.log($(this).val());
          removeMergedConcepts($(this).val());
        }
    });
    
    function mergeConcepts(conceptId) {
      $('#mergeError').hide();
      
      var conceptsToMerge = $('#conceptIdsToMerge').val(); //retrieve array
      if(conceptsToMerge != '') {
        console.log(conceptsToMerge);
        conceptsToMerge = JSON.parse(conceptsToMerge);  
      } else {
        conceptsToMerge = [];
        console.log('Created a new array');
      }
      conceptsToMerge.push(conceptId);

      $('#conceptIdsToMerge').val(JSON.stringify(conceptsToMerge));
      console.log(JSON.stringify(conceptsToMerge));

      /*var conceptSearchResultTable = $('#conceptSearchResult').dataTable();
        $.each($("input[name='conceptMergeCheckbox']:checked", conceptSearchResultTable.fnGetNodes()), function(){            
          conceptIdsToMerge.push($(this).val());
        });*/
        
    }    

    function removeMergedConcepts(conceptId) {
        var conceptsToMerge = $('#conceptIdsToMerge').val();
        conceptsToMerge = JSON.parse(conceptsToMerge);  
        conceptsToMerge.splice($.inArray(conceptId, conceptsToMerge), 1);
        $('#conceptIdsToMerge').val(JSON.stringify(conceptsToMerge));
        console.log(JSON.stringify(conceptsToMerge));
    }

    $("#mergeConcept").hide();
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
  var conceptIdsToMerge = $('#conceptIdsToMerge').val();
  var test = document.getElementById('conceptIdsToMerge');
  console.log(test);
  conceptIdsToMerge = JSON.parse(conceptIdsToMerge);
  if(conceptIdsToMerge.length < 2) {
      $('#mergeError').show();
    } else {
        console.log(conceptIdsToMerge);
        console.log(conceptIdsToMerge.join(","));
        window.location = '${pageContext.servletContext.contextPath}/auth/concepts/merge?mergeIds=' + conceptIdsToMerge.join(",");      
    }
}

function paginate(page, sortDir, sortColumn, word, pos) {
      console.log('Pagination called');
      var conceptIdsToMerge = $('#conceptIdsToMerge').val();
      conceptIdsToMerge = JSON.parse(conceptIdsToMerge);
      console.log(conceptIdsToMerge);
      window.location = '${pageContext.servletContext.contextPath}/home/conceptsearch?page=' + ${page - 1} + '&sortDir=' + ${sortDir} + '&sortColumn=' + ${sortColumn} + '&word=' + word + '&pos=' + pos + '&conceptIdsToMerge=' + conceptIdsToMerge;
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

var createWrapper = function(word, pos, conceptList, description, conceptType, wrapperId) {
  window.location = '${pageContext.servletContext.contextPath}/auth/conceptlist/addconceptwrapper?word=' + word + '&selectedConceptList=' + conceptList + '&description=' + description + '&selectedType=' + conceptType + '&wrapperids=' + wrapperId;
}

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
  method='get' commandName='conceptSearchBean'>
  <form:errors path="luceneError"></form:errors>
  
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

  <table cellpadding="0" cellspacing="0"
    class="table table-striped table-bordered" id="conceptSearchResult">
    <thead>
      <tr>
        <sec:authorize access="isAuthenticated()">
          <th data-name='edit'></th>
          <th data-name='delete'></th>
          <th data-name='merge'></th>
        </sec:authorize>
        <th>Term</th>
        <th>ID</th>
        <th>Wordnet ID</th>
        <th>POS</th>
        <th>Concept List</th>
        <th>Description</th>
        <th>Type</th>
      </tr>
    </thead>
    <tbody>
      <c:forEach var="concept"
        items="${conceptSearchBean.foundConcepts}">
        <tr class="gradeX" title="${concept.uri}">
          <sec:authorize access="isAuthenticated()">
            <td><c:choose>
                <c:when
                  test="${not fn:containsIgnoreCase(concept.entry.id, 'WID')}">
                  <a
                    href="${pageContext.servletContext.contextPath}/auth/conceptlist/editconcept/${concept.entry.id}?fromHomeScreen=true">
                    <i title="Edit Concept"
                    class="fa fa-pencil-square-o"></i>
                  </a>
                </c:when>
                <c:otherwise>
                  <a href="#" onclick="createWrapper('${concept.entry.word}', '${concept.entry.pos}', '${concept.entry.conceptList}', '${concept.entry.description}', '${concept.entry.typeId}', '${concept.entry.id}');">
                    <i title='Create wrapper' class="fa fa-pencil-square-o"></i>
                  </a>
                </c:otherwise>
              </c:choose></td>

            <td><c:choose>
                <c:when
                  test="${not fn:containsIgnoreCase(concept.entry.id, 'WID')}">
                  <a
                    href="${pageContext.servletContext.contextPath}/auth/conceptlist/deleteconcept/${concept.entry.id}?fromHomeScreenDelete=true"
                    id="${concept.entry.id}"><i
                    title="Delete Concept" class="fa fa-trash"></i></a>
                  </font>

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
          <td align="justify"><font size="2"> <a
              id="${concept.entry.id}" data-toggle="modal"
              data-target="#detailsModal"
              data-conceptid="${concept.entry.id}"><c:out
                  value="${concept.entry.word}"></c:out></a></font></td>
          <td align="justify"><font size="2"><c:out
                value="${concept.entry.id}"></c:out></font></td>
          <td align="justify"><font size="2"><c:out
                value="${concept.entry.wordnetId}"></c:out></font></td>
          <td align="justify"><font size="2"><c:out
                value="${concept.entry.pos}"></c:out></font></td>
          <td align="justify"><font size="2"><c:out
                value="${concept.entry.conceptList}"></c:out></font></td>
          <td align="justify">
                <div class="scrollable" style="max-height: 100px; max-width: 400px;">
                    <c:out value="${concept.description}" escapeXml="false"></c:out>
                </div>
          </td>
          <td align="justify"><font size="2"><c:out
                value="${concept.type.typeName}"></c:out></font></td>
        </tr>
      </c:forEach>
    </tbody>
  </table>

  <input:hidden value='' name='conceptIdsToMerge' id='conceptIdsToMerge' />
  <nav aria-label="Page navigation">
      <ul class="pagination">
        <li <c:if test="${page == 1}">class="disabled"</c:if>>
          <a <c:if test="${page > 1}"> href="#" onclick="paginate('${page - 1}', '${sortDir}', '${sortColumn}', '${conceptSearchBean.word}', '${conceptSearchBean.pos}');"</c:if> aria-label="Previous" >
            <span aria-hidden="true">&laquo;</span>
          </a>
        </li>
    <c:forEach begin="1" end="${count}" var="val">
        <li <c:if test="${val == page}">class="active"</c:if>><a href="#" onclick="paginate('${val}', '${sortDir}', '${sortColumn}', '${conceptSearchBean.word}', '${conceptSearchBean.pos}');" ><c:out value="${val}"/></a></li>
    </c:forEach>
        <li <c:if test="${page == count}">class="disabled"</c:if>>
          <a <c:if test="${page < count}"> href="#" onclick="paginate('${page + 1}', '${sortDir}', '${sortColumn}', '${conceptSearchBean.word}', '${conceptSearchBean.pos}');"</c:if> aria-label="Next">
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
          </tr>
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

      </div>
    </div>
  </div>
  </div>