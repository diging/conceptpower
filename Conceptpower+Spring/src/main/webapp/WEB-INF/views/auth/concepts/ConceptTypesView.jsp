<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>


<script type="text/javascript">
    var sortColumn = function(sortColumnName) {
        var sortDir = ${sortDir};
        console.log(sortDir);
        if(sortDir === -1) {
            sortDir = 1;
        } else {
            sortDir = -1;
        }
        var pageNumber = ${page};
        var url = "${pageContext.servletContext.contextPath}/auth/concepttype?page="+pageNumber+"&sortDir="+sortDir+"&sortColumn="+sortColumnName;   
        window.location = url;  
    }
</script>


<h1>Types</h1>
<br />
<a
	href="${pageContext.servletContext.contextPath}/auth/concepttype/addtype"><i
	class="fa fa-plus-circle"></i> Add New Type</a>

<br />
<br />
<p>See existing types below.</p>

<c:if test="${not empty result}">
	<table cellpadding="0" cellspacing="0" class="table table-striped table-bordered"
		id="conceptTypes">
		<thead>
			<tr>
				<th></th>
				<th></th>
				<th>
                    <a href="#" onclick="sortColumn('typeName')" />Name</a>
                </th>
				<th>
                    <a href="#" onclick="sortColumn('typeId')" />Type URI</a>
                </th>
				<th>
                    <a href="#" onclick="sortColumn('description')" />Description</a>
                </th>
				<th>
                    <a href="#" onclick="sortColumn('supertypeId')" />Super Type</a>
                </th>
				<th>
                    <a href="#" onclick="sortColumn('matches')" />Matches</a>
                </th>
				<th>
                    <a href="#" onclick="sortColumn('creatorId')" />Created by</a>
                </th>
				<th>
                    <a href="#" onclick="sortColumn('modified')" />Modified</a>
                </th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="types" items="${result}">
				<tr class="gradeX">
					<td align="justify"><a
						href="${pageContext.servletContext.contextPath}/auth/concepttype/deletetype/${types.typeId}">
						<i class="fa fa-trash-o"></i>
						</a>
					</td>
					<td align="justify"><a
						href="${pageContext.servletContext.contextPath}/auth/concepttype/edittype/${types.typeId}"><i
							class="fa fa-pencil-square-o"></i></a></td>
					<td align="justify"><font size="2"><c:out
								value="${types.typeName}"></c:out></font></td>
					<td align="justify"><font size="2"><c:out
								value="${types.typeId}"></c:out></font></td>
					<td align="justify"><font size="2"><c:out
								value="${types.description}" escapeXml="false"></c:out></font></td>
					<td align="justify"><font size="2"><c:out
								value="${types.supertypeId}"></c:out></font></td>
					<td align="justify"><font size="2"><c:out
								value="${types.matches}"></c:out></font></td>
					<td align="justify"><font size="2"><c:out
								value="${types.creatorId}"></c:out></font></td>
					<td align="justify"><font size="2"><c:out
								value="${types.modified}"></c:out></font></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
    
    <nav aria-label="Page navigation">
      <ul class="pagination">
        <li <c:if test="${page == 1}">class="disabled"</c:if>>
          <a <c:if test="${page > 1}">href="<c:url value="/auth/concepttype?page=${page - 1}&sortDir=${sortDir}&sortColumn=${sortColumn}" />"</c:if> aria-label="Previous">
            <span aria-hidden="true">&laquo;</span>
          </a>
        </li>
    <c:forEach begin="1" end="${count}" var="val">
        <li <c:if test="${val == page}">class="active"</c:if>><a href="<c:url value="/auth/concepttype?page=${val}&sortDir=${sortDir}&sortColumn=${sortColumn}" />"><c:out value="${val}"/></a></li>
    </c:forEach>
        <li <c:if test="${page == count}">class="disabled"</c:if>>
          <a <c:if test="${page < count}">href="<c:url value="/auth/concepttype?page=${page + 1}&sortDir=${sortDir}&sortColumn=${sortColumn}" />"</c:if> aria-label="Next">
            <span aria-hidden="true">&raquo;</span>
          </a>
        </li>
      </ul>
    </nav>

</c:if>