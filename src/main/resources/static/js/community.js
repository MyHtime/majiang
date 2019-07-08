function post_comment() {
    var question_id = $("#question_id").val();
    var comment_content = $("#comment_content").val();
    if (!comment_content) {
        alert("回复内容不能为空！");
        return;
    }
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
                window.location.reload();
            } else {
                if (data.code == 2003) {
                    var confirm = window.confirm(data.message);
                    if (confirm) {
                        window.localStorage.setItem("closable", "true");
                        window.open("https://github.com/login/oauth/authorize?client_id=6ec56ffc2179dd5c6116&redirect_uri=http://localhost:8887/callback&scope=user&state=1");
                    }
                } else {
                    alert(data.message);
                }
            }
        }
    });
}