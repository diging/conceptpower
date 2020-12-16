$(document).on('click', '#conceptDetails', function(e) {
    var conceptid = $(this).data('conceptid');

    $.ajax({
        type: "GET",
        url: [['/conceptpower/conceptDetail']],
        data: {
            conceptid: conceptid
        },
        success: function(details) {
            details = $.parseJSON(details);
            $("#conceptDetailsName").text(details.name);
            $("#conceptDetailsId").text(details.id);
            $("#conceptDetailsUri").text(details.uri);
            $("#conceptDetailsWordnetId").text(details.wordnetid);
            $("#conceptDetailsPos").text(details.pos);
            $("#conceptDetailsList").text(details.conceptlist);
            $("#conceptDetailsType").text(details.type);
            $("#conceptDetailsEqualTo").text(details.equalto);
            $("#conceptDetailsSimilarTo").text(details.similarto);
            $("#conceptDetailsCreator").text(details.creator);
            $("#conceptDetailsDesc").html(details.description);
            $("#conceptDetailsMergedIds").html(details.mergedIds);

            if (details.mergedIds) {
                $.ajax({
                    url: "[[@{'/rest/OriginalConcepts'}]]",
                    type: 'GET',
                    headers: {
                        Accept: "application/json",
                    },
                    data: {
                        ids: details.mergedIds
                    },
                    success: function(originalConcepts) {
                        var mergedIdsHtml = '';
                        $.each(originalConcepts.conceptEntries, function(index, conceptEntry) {
                            if (mergedIdsHtml) {
                                mergedIdsHtml += ", ";
                            }
                            mergedIdsHtml += '<span title =  "Id: ' + conceptEntry.id + ', Word: ' + conceptEntry.lemma + ', Description: ' + conceptEntry.description + '">';
                            mergedIdsHtml += "<i>" + conceptEntry.id + "</i>";
                            mergedIdsHtml += "</span>";
                        });
                        $("#conceptDetailsMergedIds").html(mergedIdsHtml);
                    }
                });
            }
        }
    });
});

function getListOfReviewRequests(conceptId, operation) {
    $.ajax({
        type: "GET",
        url: [['/conceptpower/auth/request/']] + conceptId + "/all",
        success: function(response) {
            var reviewHistory = '';
            reviewHistory += '<div id="review-history-' + conceptId + '">';
            $.each(response, function(index, element) {
                var reviewNumber = index + 1;
                reviewHistory += '<div>';
                reviewHistory += '<div  class="reviewhistory">' +
                    '<h4 data-toggle="tooltip" data-placement="right"><b>' + reviewNumber + '. ' + element.request + '</b></h4></div>'
                reviewHistory += '<div  id="review-' + index + conceptId + '">'

                $.each(element.comments, function(iterator, commentElement) {
                    reviewHistory += '<div><h6 style="color:#19586B; display:inline-block;">' + commentElement.createdBy.toUpperCase() + ': </h6><span>' + '  ' + commentElement.comment + '</span></div>';
                });

                reviewHistory += '</div></div>';
            });

            reviewHistory += '</div>';
            if (operation === 'resolve') {
                $('#reviewHistoryForResolve').html(reviewHistory);
            } else {
                $('#reviewHistoryForReopen').html(reviewHistory);
            }
        },
        error: function(e) {
            if (operation === 'resolve') {
                $('reopenError').text('Fetching Review History Failed')
            } else {
                $('resolveCommentError').text('Fetching Review History Failed')
            }
        }
    });
}

$(document).on("click", ".fa-exclamation-triangle", function() {
    $('#reviewId').val($(this).data("review-id"));
    $("#request").val($(this).data("request"));
    $("#conceptId").val($(this).data("conceptId"));
    $('#resolveCommentError').hide();
    $("#requestBox").show();

    getListOfReviewRequests($("#conceptId").val(), 'resolve');
});