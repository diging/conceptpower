<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec"
    uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<head>
<title>Merge Concept</title>
<form:form
    action="${pageContext.servletContext.contextPath}/auth/mergeConcepts"
    method='post' modelAttribute="mergeConceptsBean">

    <h1>Merge concepts</h1>

    <table>
        <tr>
            <td class="col-sm-1">Concept</td>
            <td class="col-sm-11"><form:input path="word"
                    class="form-control" /></td>
        </tr>
        <tr>
            <td class="col-sm-1">POS</td>
            <td class="col-sm-11"><form:select
                    path="selectedPosValue" class="form-control">
                    <form:option value="" />
                    <form:options items="${conceptsMergeBean.posMap}" />
                </form:select></td>
        </tr>
        <tr>
            <td class="col-sm-1">Concept List</td>
            <td class="col-sm-11"><form:select
                    path="conceptListValue" class="form-control"
                    items="${conceptsMergeBean.conceptList}"
                    itemValue="conceptListName"
                    itemLabel="conceptListName" /></td>
        </tr>
        <tr>
            <td class="col-sm-1">Description</td>
            <td class="col-sm-11"><form:textarea path="description"
                    rows="7" cols="50" class="form-control" /></td>
        </tr>
        <tr>
            <td class="col-sm-1">Synonyms</td>
            <td class="col-sm-11">
                <div id="addedSynonyms" hidden="true">
                    <table border="1" width="400"
                        id="addedSynonymsTable" hidden="true"
                        class="table table-striped table-bordered">
                        <tbody>
                        </tbody>
                    </table>

                </div>

            </td>
            <td><form:hidden path="synonymsids"></form:hidden> <input
                type="button" name="synonym" id="addsynonym"
                data-toggle="modal" data-target="#synonymModal"
                value="Add Synonym" class="btn-xs btn-primary"></td>
        </tr>

        <tr>
            <td class="col-sm-1">Concept Type</td>
            <td class="col-sm-11"><form:select
                    path="selectedTypeId" class="form-control">
                    <form:option value="" />
                    <form:options items="${conceptsMergeBean.types}"
                        itemValue="typeId" itemLabel="typeName" />
                </form:select></td>
        </tr>
        <tr>
            <td class="col-sm-1">Equals</td>
            <td class="col-sm-11"><form:textarea path="equals"
                    class="form-control" />
        </tr>

        <tr>
            <td class="col-sm-1">Similar</td>
            <td class="col-sm-11"><form:input path="similar"
                    class="form-control" /></td>
        </tr>

        <tr>
            <td class="col-sm-1">Wordnet</td>
            <td class="col-sm-11"><form:textarea path="wordnetIds"
                    id="wordnetIds" class="form-control" /></td>
        </tr>

    </table>
    <br />
    <br />

    <table class="width: 100%;">
        <tr>
            <td><input type="submit" name="edit" id="edit"
                value="Store merged concept" class="btn btn-primary"></td>

            <td><a
                href="${pageContext.servletContext.contextPath}/auth/concepts/canceledit?fromHomeScreen=true"><input
                    type="button" name="cancel" value="Cancel!"
                    class="btn btn-primary"></a></td>
        </tr>
    </table>
</form:form>