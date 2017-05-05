<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec"
    uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page session="false"%>

<h1>Concept Details: ${entry2.id} </h1>

<div class='container'>
    <table class='table table-striped table-hover table-sm table-bordered' style="width: auto !important;">
        <tr>
            <td>
                <b>Word</b>
            </td>
            <td>
                ${entry.word}
            </td>
        </tr>
        <tr>
            <td>
                <b>Description</b>
            </td>
            <td>
                ${entry.description}
            </td>
        </tr>
        <tr>
            <td>
                <b>POS</b>
            </td>
            <td>
                ${entry.pos}
            </td>
        </tr>
        <tr>
            <td>
                <b>Concept List</b>
            </td>
            <td>
                ${entry.conceptList}
            </td>
        </tr>
        <tr>
            <td>
                <b>WordnetId</b>
            </td>
            <td>
                ${entry.wordnetId}
            </td>
        </tr>
        <tr>
            <td>
                <b>Synonym Ids</b>
            </td>
            <td>
                ${entry.synonymIds}
            </td>
        </tr>
        <tr>
            <td>
                <b>Alternative Ids</b>
            </td>
            <td>
                <c:forEach items="${entry.alternativeIds}" var="alternativeId">
                    <c:out value="${alternativeId}" />, 
                </c:forEach>
            </td>
        </tr>
        <tr>
            <td>
                <b>URI</b>
            </td>
            <td>
                ${uri}
            </td>
        </tr>
        <tr>
            <td>
                <b>Creator</b>
            </td>
            <td>
                ${entry.creatorId}
            </td>
        </tr>
    </table>
</div>