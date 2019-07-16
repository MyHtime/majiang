/**
 * 提交回复
 */
function post_comment() {
    var question_id = $("#question_id").val();
    var comment_content = $("#comment_content").val();
    comment2target(question_id, 1, comment_content);
}

function comment2target(targetId, type, content) {
    if (!content) {
        alert("回复内容不能为空！");
        return;
    }
    $.ajax({
        type: "POST",
        url: "/comment",
        dataType: "json",
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify({
            parentId: targetId,
            content: content,
            type: type
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

/**
 * 添加二级评论
 */
function sub_comment(e) {
    var comment_id = e.getAttribute("data-id");
    var content = $("#input-"+comment_id).val();
    comment2target(comment_id, 2, content);
}

//<!--                                <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 comments" th:each="comment : ${comments}">-->
// <!--                                    <div class="media">-->
// <!--                                        <div class="media-left">-->
// <!--                                            <img class="media-object img-rounded" th:src="${comment.user.avatarUrl}">-->
// <!--                                        </div>-->
// <!--                                        <div class="media-body">-->
// <!--                                            <h5 class="media-heading" th:text="${comment.user.name}">-->
// <!--                                            </h5>-->
// <!--                                            <div th:text="${comment.content}"></div>-->
// <!--                                            <div class="menu pull-right" th:text="${#dates.format(comment.gmtCreate, 'yyyy-MM-dd')}">-->
// <!--                                            </div>-->
// <!--                                        </div>-->
// <!--                                    </div>-->
// <!--                                </div>-->

/**
 * 展开/折叠二级评论
 */
function collapseComments(e) {
    var id = e.getAttribute("data-id");
    var $comment = $("#comment-"+id);

    //获取二级评论展开状态
    var collapse = e.getAttribute("data-collapse");
    if (collapse) {
        //折叠二级评论
        $comment.removeClass("in");
        e.removeAttribute("data-collapse");
        e.classList.remove("active");
                                                                                                                                                                                                                                                                                                                                $comment.children(".comments").remove();
    } else {

        $.getJSON("/comment/" +id,function (data) {
            $.each(data.data, function (index, comment) {

                var $mediaLeftElement = $("<div/>", {
                    "class": "media-left"
                }).append($("<img/>", {
                    "class": "media-object img-rounded",
                    "src": comment.user.avatarUrl
                }));

                var $mediaBodyElement = $("<div/>", {
                    "class": "media-body"
                }).append($("<h5/>", {
                    "class": "media-heading",
                    "text": comment.user.name
                })).append($("<div/>", {
                    "text": comment.content
                })).append($("<div/>", {
                    "class": "menu pull-right",
                    "text": moment(comment.gmtCreate).format("YYYY-MM-DD HH:mm:ss")
                }));

                var $mediaElement = $("<div/>", {
                    "class": "media"
                }).append($mediaLeftElement).append($mediaBodyElement);

                var $commentElement = $("<div/>", {
                    "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments"
                }).append($mediaElement);

                $comment.prepend($commentElement);
            });

            //展开二级评论
            $comment.addClass("in");
            //标记二级评论展开状态
            e.setAttribute("data-collapse", "in");
            e.classList.add("active");
        });
    }

}

function selectTag(e) {
    var value = e.getAttribute("data-tag");
    var $tag = $("#tag");
    var previous = $tag.val();

    if (previous.indexOf(value) == -1) {
        if (previous) {
            $tag.val(previous + "," + value);
        } else {
            $tag.val(value);
        }
    }
}

function showSelectTag() {
    $("#selectTag").show();
}