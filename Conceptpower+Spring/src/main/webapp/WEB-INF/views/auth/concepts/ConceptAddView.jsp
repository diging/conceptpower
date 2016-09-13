<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec"
    uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page session="false"%>


<script>
    $(function() {
        $(document).ready(function() {
            var term = $('#term').val();
            if (term != '') {
                var synonymDescription = $('#synonymDescription').val();
                var id = $('#synonymId').val();
                var termArray = term.split(',,');
                var synonymDescriptionArray = synonymDescription.split(',,');
                var idArray = id.split(',,');
                for (var i = 0; i < termArray.length; i++) {
                    if (idArray[i] != '') {
                        synonymAdd(idArray[i], termArray[i], synonymDescriptionArray[i], 'true');
                    }
                }
            }
        });
        $("#synonymModal").dialog({
            autoOpen : false
        });
    });
    $(function() {
        $("#addsynonym").click(function() {
            var addedSynonymsTable = $('#addedSynonyms');
            $('#synonymstable').dataTable().fnClearTable();
            $("#synonymModal").dialog('open');
            $("#synonymsDialogTable").show();
        });
        $("#name").on(
                "change paste",
                function() {
                    $.ajax({
                        type : "GET",
                        url : "${pageContext.servletContext.contextPath}/getExistingConcepts",
                        data : {
                            conceptname : $(this).val()
                        },
                        success : function(response) {
                            var data = jQuery.parseJSON(response);
                            if (data.length > 0) {
                                var text = "Following concepts have the same name : \n";
                                var len = data.length;
                                for (var i = 0; i < len; i++) {
                                    text += (data[i].word + " - " + data[i].description + "\n");
                                }
                                $("#warning").show();
                                $('#warning').prop('title', text);
                            } else {
                                $("#warning").hide();
                                $('#warning').prop('title', '');
                            }
                        },

                        error : function(httpStatus, response) {
                            if (httpStatus.status == 409) {
                                var errorMessage = "<i class=\"fa fa-exclamation-triangle\">"
                                        + httpStatus.responseText + "</i>";
                                $("#synonymModal").dialog("close");
                                $('#synonymModal').modal('toggle');
                                $('#errorMessage').show();
                                $('#error_alert_msg').html(errorMessage);
                            }
                        }
                    });
                });
    });
    $(document).ready(definedatatable);
    function definedatatable() {
        $('#synonymstable').dataTable(
                {
                    "bJQueryUI" : true,
                    "sPaginationType" : "full_numbers",
                    "bAutoWidth" : false,
                    "bStateSave" : true,
                    "aoColumns" : [ {
                        "sTitle" : "Term",
                        "mDataProp" : "word",
                    }, {
                        "sTitle" : "POS",
                        "mDataProp" : "pos",
                    }, {
                        "sTitle" : "Description",
                        "mDataProp" : "description",
                    }, {
                        "sTitle" : "Add",
                        "mDataProp" : "id"
                    } ],
                    "fnRowCallback" : function(nRow, aData, iDisplayIndex) {
                        var description = aData.description;
                        description = description != null ? description.replace(/"/g, '&quot;')
                                : "";
                        $('td:eq(3)', nRow).html(
                                '<a onclick="synonymAdd(\'' + aData.id + '\',\'' + aData.word
                                        + '\',\'' + description + '\')">Add</a>');
                        return nRow;
                    }
                });
    };
    $(function() {
        $("#synonymsearch").click(
                function() {
                    $("#synonymViewDiv").show();
                    $("#synonymstable").show();
                    var addedsynonym = $('#addedSynonnym').val();
                    var synonymname = $("#synonymname").val();
                    $.ajax({
                        type : "GET",
                        url : "${pageContext.servletContext.contextPath}/conceptAddSynonymView",
                        data : {
                            synonymname : synonymname,
                            addedsynonym : addedsynonym
                        },
                        success : function(response) {
                            var data = jQuery.parseJSON(response);
                            $('#synonymstable').dataTable().fnClearTable();
                            $('#synonymstable').dataTable().fnAddData(data);
                        },
                        error : function(httpStatus, response) {
                            if (httpStatus.status == 409) {
                                var errorMessage = "<i class=\"fa fa-exclamation-triangle\">"
                                        + httpStatus.responseText + "</i>";
                                $("#synonymModal").dialog("close");
                                $('#synonymModal').modal('toggle');
                                $('#errorMessage').show();
                                $('#error_alert_msg').html(errorMessage);
                            }
                        }
                    });
                });
    });
    var synonymAdd = function(id, term, description, onLoad) {
        if (typeof onLoad === "undefined") {
            onLoad = false;
        }
        if (onLoad == false) {
            $("#dialog").dialog("close");
            $("#synonymModal").dialog("close");
            $('#synonymModal').modal('toggle');
            $("#synonymsDialogTable").hide();
        }
        var x = document.getElementById('addedSynonymsTable');
        var addedsynonym = $('#addedSynonnym').val();
        if (x != null) {
            var new_row = x.rows[0].cloneNode(true);
            new_row.cells[0].innerHTML = '<a onclick="synonymRemove(\'' + x.rows.length
                    + '\')">Remove</a>' + '</font></td>'
            new_row.cells[1].innerHTML = term;
            new_row.cells[2].innerHTML = description;
            new_row.cells[3].innerHTML = id;
            new_row.cells[3].hidden = true;
            if (onLoad == false) {
                var t = $('#term').val();
                t = t + ",," + term;
                $('#term').val(t);
                var d = $('#synonymDescription').val();
                d = d + ",," + description;
                $('#synonymDescription').val(d);
                var i = $('#synonymId').val();
                i = i + ",," + id;
                $('#synonymId').val(i);
            }
            x.appendChild(new_row);
            addedsynonym += ','
            addedsynonym += id;
            $('#addedSynonnym').val(addedsynonym);
        } else {
            var html = '<table border="1" width="400" id="addedSynonymsTable"><thead></thead>';
            html += '<tr><td align="justify"><font size="2">'
                    + '<a onclick="synonymRemove(\'0\')">Remove</a>' + '</font></td>';
            html += '<td align="justify"><font size="2">' + term + '</font></td>';
            html += '<td align="justify"><font size="2">' + description + '</font></td>';
            html += '<td align="justify" hidden="true">' + id + '</td></tr>';
            html += '</table>';
            html += '<input type="hidden" name="syns" />';
            addedsynonym += id;
            if (onLoad == false) {
                $('#term').val(term);
                $('#synonymDescription').val(description);
                $('#synonymId').val(id);
            }
            $('#addedSynonnym').val(addedsynonym);
            $("#addedSynonyms").html(html);
        }
        var synonyms = " ";
        var table = document.getElementById('addedSynonymsTable');
        for (var r = 0, n = table.rows.length; r < n; r++) {
            synonyms += table.rows[r].cells[3].innerHTML + ',';
        }
        $("#synonymsids").val(synonyms);
    };
    var synonymRemove = function(row) {
        bindingRemoval(row);
        $('#addedSynonnym').val('');
        var x = document.getElementById('addedSynonymsTable');
        x.deleteRow(row);
        if (!(x.rows.length > 0))
            x.parentNode.removeChild(x);
        var synonyms = " ";
        var table = document.getElementById('addedSynonymsTable');
        for (var r = 0, n = table.rows.length; r < n; r++) {
            synonyms += table.rows[r].cells[3].innerHTML + ',';
        }
        $("#synonymsids").val(synonyms);
        $('#addedSynonnym').val(synonyms);
    };
    var bindingRemoval = function(row) {
        var ids = $('#synonymId').val();
        var idArray = ids.split(',,');
        var newIdArray = '';
        for (var i = 0; i < idArray.length; i++) {
            if (i != row) {
                newIdArray = newIdArray + idArray[i];
                newIdArray = newIdArray + ',,';
            }
        }
        $('#synonymId').val(newIdArray);
        var synonymDescriptions = $('#synonymDescription').val();
        var synonymDescriptionArray = synonymDescriptions.split(',,');
        var newSynonymArray = '';
        for (var i = 0; i < synonymDescriptionArray.length; i++) {
            if (i != row) {
                newSynonymArray = newSynonymArray + synonymDescriptionArray[i];
                newSynonymArray = newSynonymArray + ',,';
            }
        }
        $('#synonymDescription').val(newSynonymArray);
        var terms = $('#term').val();
        var termArray = terms.split(',,');
        var newTermArray = '';
        for (var i = 0; i < termArray.length; i++) {
            if (i != row) {
                newTermArray = newTermArray + termArray[i];
                newTermArray = newTermArray + ',,';
            }
        }
        $('#term').val(newTermArray);
    }
    //service 
    $(function() {
        $("#serviceSearch").click(function() {
            var serviceterm = $("#serviceterm").val();
            var serviceid = $("#selectedServiceNameIdMap").val();
            $.ajax({
                type : "GET",
                url : "${pageContext.servletContext.contextPath}/serviceSearch",
                data : {
                    serviceterm : serviceterm,
                    serviceid : serviceid
                },
                success : function(response) {
                    if (response.length > 0) {
                        var data = jQuery.parseJSON(response);
                        $('#serviceResultTable').dataTable().fnClearTable();
                        $('#serviceResultTable').dataTable().fnAddData(data);
                        $('#showServiceResult').show();
                    } else {
                        $('#serviceResultTable').dataTable().fnClearTable();
                        $('#showServiceResult').show();
                    }
                }
            });
        });
    });
    $(function() {
        $("#showServiceResult").click(function() {
            $('#serviceResult').toggle();
            $('#serviceResultTable').toggle();
        });
    });
    $(document).ready(defineServicedatatable);
    function defineServicedatatable() {
        $('#serviceResultTable').dataTable(
                {
                    "bJQueryUI" : true,
                    "sPaginationType" : "full_numbers",
                    "bAutoWidth" : false,
                    "aoColumns" : [ {
                        "sTitle" : "Select",
                        "mData" : "isChecked",
                    }, {
                        "sTitle" : "Word",
                        "mDataProp" : "word",
                    }, {
                        "sTitle" : "ID",
                        "mDataProp" : "id",
                    }, {
                        "sTitle" : "Description",
                        "mDataProp" : "description",
                    } ],
                    aoColumnDefs : [
                            {
                                "targets" : [ 0, 1 ],
                                'bSortable' : false
                            },
                            {
                                aTargets : [ 0 ],
                                mRender : function(sourceData, dataType, fullData) {
                                    var check = '';
                                    if ($('#equals').val().indexOf(fullData.id) != -1) {
                                        check = 'checked=\'checked\'';
                                    }
                                    var word = encodeURI(fullData.word);
                                    return '<input type="checkbox" id="' + fullData.id + '" '
                                            + check
                                            + ' name="isChecked" onclick="serviceConceptAdd(\''
                                            + fullData.id + '\',\'' + word.replace(/'/g, '%39')
                                            + '\')"></input>';
                                },
                            } ],
                    "order" : [ [ 1, "desc" ] ]
                });
    };
    var serviceConceptAdd = function(serviceConceptID, name) {
        var check = false;
        if (document.getElementById(serviceConceptID).checked) {
            $('#equals').val(serviceConceptID);
            document.getElementById("name").value = decodeURI(name);
            check = true;
        } else if (!(document.getElementById(serviceConceptID).checked)) {
            $('#equals').val('');
            document.getElementById("name").value = '';
            check = false;
        }
        var checkboxes = new Array();
        checkboxes = document.getElementsByTagName('input');
        for (var i = 0; i < checkboxes.length; i++) {
            if (checkboxes[i].type == 'checkbox') {
                checkboxes[i].checked = false;
            }
        }
        if (check)
            document.getElementById(serviceConceptID).checked = true;
        else
            document.getElementById(serviceConceptID).checked = false;
    };
    
    $(document).ready(function() { 
        $('#createconceptform').submit(function(event) {
            var ret = true;
            if ($("#name").val() === "") {
                $("#nameerror").html('Please enter a name for concept.');
                ret = false;
            } else {
                $("#nameerror").html('');
            }
            if ($("#pos").val() === "") {
                $("#poserror").html('Please select a pos.');
                ret = false;
            } else {
                $("#poserror").html('');
            }
            if ($("#selectedList").val() === "") {
                $("#listerror").html('Please select a concept list.');
                ret = false;
            } else {
                $("#listerror").html('');
            }
            if ($("#selectedTypes").val() === "") {
                $("#typeerror").html('Please select a concept type.');
                ret = false;
            } else {
                $("#typeerror").html('');
            }
            return ret;
        });    
    });
    
    $(document).ready(showAjaxProcessing);
    function showAjaxProcessing() {
        var $loading = $('#loadingDiv').hide();
        $(document).ajaxStart(function() {
            $loading.show();
        }).ajaxStop(function() {
            $loading.hide();
        });
    }
    $(document).ready(hideFormProcessing);
    function hideFormProcessing() {
        $('#loadingDiv').hide();
    }
    function showFormProcessing() {
        $('#loadingDiv').show();
    }
</script>

<table style="padding: 0px;">
    <tr>
        <td><h1>Add new concept</h1></td>
    </tr>
    <tr>
        <td><div>Add a new concept here.</div></td>
    </tr>
</table>

<table>
    <tr>
        <td>
            <form:form
                action="${pageContext.servletContext.contextPath}/auth/conceptlist/addconcept/add"
                commandName="conceptAddBean" method='post'
                id="createconceptform">

                <table>
                    <tr>
                        <td style="vertical-align: top;">

                            <table>
                                <tr>
                                    <td>Service</td>
                                    <td><form:select
                                            path="selectedServiceNameIdMap"
                                            class="form-control">
                                            <form:options
                                                items="${conceptAddBean.serviceNameIdMap}" />
                                        </form:select></td>
                                    <td>Term</td>
                                    <td><input type="text"
                                        name="serviceterm"
                                        id="serviceterm"
                                        class="form-control"></td>
                                    <td><input type="button"
                                        id="serviceSearch"
                                        value="search" class="button"
                                        class="btn btn-primary" /> <img
                                        alt="" id="loadingDiv"
                                        width="16px" height="16px"
                                        src="${pageContext.servletContext.contextPath}/resources/img/ajax_process_16x16.gif"
                                        class="none"></td>
                                </tr>
                            </table>
                        </td>

                    </tr>
                </table>

                <div style="padding: 15px;" id="showServiceResult"
                    hidden="true">
                    <a>Show Results</a>
                </div>
                <div id="serviceResult"
                    style="max-width: 1000px; padding: 15px;"
                    hidden="true">

                    <table cellpadding="0" cellspacing="0"
                        class="table table-striped table-bordered"
                        id="serviceResultTable" hidden="true">
                        <tbody>
                        </tbody>
                    </table>

                </div>
                <table>
                    <tr>
                        <td>Concept &nbsp &nbsp &nbsp &nbsp</td>

                        <td>
                            <form:input path="name" class="form-control"></form:input>
                        </td>
                        <td><div id="nameerror" style="color: red"></div>
                            <form:errors path="name"
                                class="ui-state-error-text"></form:errors></td>
                    </tr>

                    <tr>
                        <td>POS</td>
                        <td><form:select path="pos"
                                class="form-control">
                                <form:option value="">Please select</form:option>
                                <form:option value="noun">Noun</form:option>
                                <form:option value="verb">Verb</form:option>
                                <form:option value="adverb">Adverb</form:option>
                                <form:option value="adjective">Adjective</form:option>
                                <form:option value="other">Other</form:option>
                            </form:select></td>
                        <td>
                            <div id="poserror" style="color: red"></div>
                            <form:errors path="pos" class="ui-state-error-text"></form:errors>
                        </td>
                    </tr>
                    <tr>
                        <td>Concept List</td>

                        <td>
                            <form:select path="selectedList" class="form-control">
                                <form:option value="">Please select</form:option>
                                <form:options items="${conceptAddBean.lists}" />
                            </form:select>
                        </td>
                        <td><div id="listerror" style="color: red"></div>
                            <form:errors path="selectedList"
                                class="ui-state-error-text"></form:errors></td>
                    </tr>
                    <tr>
                        <td>Description</td>
                        <td><form:textarea rows="7" cols="50"
                                path="description" class="form-control"></form:textarea></td>
                    </tr>
                    <tr>
                        <td>Synonyms</td>
                        <td><div id="addedSynonyms"></div></td>
                        <td><input type="button" name="synonym"
                            id="addsynonym" data-toggle="modal"
                            data-target="#synonymModal"
                            value="Add Synonym" class="btn btn-primary"></td>
                    </tr>
                    <tr>
                        <td>Concept Type</td>
                        <td>
                            <form:select path="selectedTypes" class="form-control">
                                <form:option value="">Please select</form:option>
                                <form:options items="${conceptAddBean.types}" />
                            </form:select>
                        </td>
                        <td><div id="typeerror" style="color: red"></div></td>
                    </tr>
                    <tr>
                        <td>Equals</td>
                        <td><form:textarea rows="5" cols="25"
                                path="equals" class="form-control"></form:textarea></td>
                    </tr>
                    <tr>
                        <td>Similar to</td>
                        <td><form:input path="similar"
                                class="form-control"></form:input></td>
                    </tr>
                    <tr>
                        <td><form:hidden path="synonymsids"></form:hidden>
                            <!--  Data for binding synonyms --> <form:hidden
                                path="term" /> <form:hidden
                                path="synonymDescription" /> <form:hidden
                                path="synonymId" /></td>
                    </tr>
                    <tr>
                        <td colspan="2"><input type="submit"
                            value="Add Concept" class="btn btn-primary"
                            onclick="showFormProcessing()"
                            onsubmit="hideFormProcessing()"
                            class="btn btn-primary"></td>
                    </tr>
                </table>

            </form:form>
        </td>
    </tr>
</table>

<form>
    <div class="modal fade" role="dialog" id="synonymModal">
        <div class="modal-dialog" tabindex="-1" role="dialog"
            aria-labelledby="myModalLabel" aria-hidden="true">
            <div class="vertical-alignment-helper">
                <div class="modal-dialog vertical-align-center">
                    <div class="modal-content">

                        <div class="modal-header">
                            <button type="button" class="close"
                                data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Search synonym</h4>
                            <table id="synonymsDialogTable"
                                hidden="true">
                                <tr>
                                    <td><input type="text"
                                        name="synonymname"
                                        id="synonymname"></td>
                                    <td><input type="hidden"
                                        name="addedSynonnym"
                                        id="addedSynonnym" /></td>
                                    <td><input type="button"
                                        name="synsearch"
                                        id="synonymsearch"
                                        value="Search"
                                        class="btn btn-primary"></td>
                                </tr>
                            </table>
                        </div>

                        <div class="modal-body">
                            <div id="synonymViewDiv"
                                style="max-width: 1000px; max-height: 500px;"
                                hidden="true">

                                <table cellpadding="0" cellspacing="0"
                                    id="synonymstable" hidden="true"
                                    class="table table-striped table-bordered">
                                    <tbody>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
