function post_comment() {
    var question_id = $("#question_id").val();
    var comment_content = $("#comment_content").val();
    $.ajax({
        type: "POST",
        url: "/comment",
        dataType: "json",
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify({
            parentId: question_id,
            content: comment_content,
            type: 1
        }),
        success: function (data) {
            if (data.code == 200) {
                $("#comment_section").hide();
            } else {
                alert(data.message)
            }
        }
    });
}