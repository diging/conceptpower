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
   $('#conceptSearchResult').dataTable({
		"bJQueryUI" : true,
		"sPaginationType" : "full_numbers",
		"bAutoWidth" : false,
		"aaSorting" : [],
		"aoColumnDefs" : [ 
				<sec:authorize access="isAuthenticated()">
			    {
		            "targets": [0,1],
			        'bSortable': false
				},
				{
					"targets": [2],
					'searchable': false,
					'bSortable': false,
					'orderable':false,
					'className': 'dt-body-center'
				},
				{
				   	"targets": [7],
				   	"sType" : "html",
				   	"fnRender" : function(o, val) {
  				  		if (o.startsWith("&lt;br/&gt;")) {
  				   			o = o.substring(11,o.length);
  				   		}
  						return $("<div/>").html(o).text();
					}
		    	},
		    	</sec:authorize>
  			  	<sec:authorize access="not isAuthenticated()">
  			    {
  			    	"targets": [5],
  			    	"sType" : "html",
  			    	"fnRender" : function(o, val) {
      			   		if (o.startsWith("&lt;br/&gt;")) {
      			   			o = o.substring(11,o.length);
      			   		}
  						return $("<div/>").html(o).text();
  			    	}
  			    }
  				</sec:authorize>
		]
	});
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
						$("#detailswordnetid").text(details.wordnetId);
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
});
					
$(document).ready(hideFormProcessing);
function hideFormProcessing() {
	$('#floatingCirclesG').hide();
}
function showFormProcessing() {
	$('#floatingCirclesG').show();
}

function mergeConcepts() {
	var conceptIdsToMerge = [];
	 var conceptSearchResultTable = $('#conceptSearchResult').dataTable();
    $.each($("input[name='conceptMergeCheckbox']:checked", conceptSearchResultTable.fnGetNodes()), function(){            
    	conceptIdsToMerge.push($(this).val());
    });
    window.location = '${pageContext.servletContext.contextPath}/prepareMergeConcept?conceptIds=' + conceptIdsToMerge.join(", ");
}

var conceptsToMerge = "";

function prepareMergeConcept(conceptId) {
	var tis = $(this);
	console.log(tis);
	var conceptsToMerge = $(this).attr("value");
	console.log(conceptsToMerge);
	if(conceptsToMerge != '') {
		console.log('Inside blank')
		conceptsToMerge = conceptId;
	} else {
		conceptsToMerge = conceptsToMerge + "," + conceptId;	
	}
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
  <div class="row">
    <div class="col-sm-6">
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
    <div class="col-sm-2">
        <c:if test="${not empty conceptSearchBean.foundConcepts}">
            <input type="button" value="Select to Merge" class="btn btn-action" onclick="mergeConcepts();">
        </c:if>
        <c:if test="${empty conceptSearchBean.foundConcepts}">
            <input disabled type="button" value="Merge Concepts" class="btn btn-action">
        </c:if>
      
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

<h3>Results</h3>

<c:if test="${not empty conceptSearchBean.foundConcepts}">
  <table cellpadding="0" cellspacing="0"
    class="table table-striped table-bordered" id="conceptSearchResult">
    <thead>
      <tr>
        <sec:authorize access="isAuthenticated()">
          <th></th>
          <th></th>
          <th>Merge</th>
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
                  <i title="Cannot edit Word Net concepts"
                    class="fa fa-pencil-square-o disabled"></i>
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
              </c:choose></td>
              
              <td>
                <input type="checkbox" name="conceptMergeCheckbox" value="${concept.entry.id }"/>
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