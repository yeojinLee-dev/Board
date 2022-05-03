var main = {
    init : function () {
        var _this = this;
        $('#btn-join').on('click', function () {
            _this.join();
        });
        $('#btn-check-duplicate').on('click', function () {
            _this.checkLoginId();
        });
        $('#password2').on('keyup', function () {
            _this.checkPassword();
        });
        $('#btn-login').on('click', function () {
            _this.login();
        });
        $('#btn-save').on('click', function () {
            _this.savePost();
        });
        $('#btn-update').on('click', function () {
            _this.updatePost();
        });
        $('#btn-delete').on('click',function () {
            _this.deletePost();
        });
        $('#btn-post-search').on('click', function () {
            _this.searchPost();
        });
        $('#btn-comment').on('click', function () {
            _this.saveComment();
        })

        //console.log('init()');
    },
    join : function () {
        var data = {
            name : $('#name').val(),
            login_id : $('#login_id').val(),
            password : $('#password2').val(),
            email : $('#email').val(),
            phone : $('#phone').val()
        };

        $.ajax({
            type : 'POST',
            url : '/api/member/join',
            dataType : 'json',
            contentType : 'application/json; charset=utf-8',
            data : JSON.stringify(data)
        }).done(function () {
            alert('회원가입이 되었습니다.');
            window.location.href = '/';
        }).fail(function (error){
            alert(JSON.stringify(error));
        });
    },
    checkLoginId : function () {
        var data = {
            login_id: $('#login_id').val()
        };

        $.ajax({
            url: '/api/member/join/check/login-id',
            type : 'POST',
            data : data,
            success : function (isDuplicateId) {
                //alert(isDuplicateID);
                if (isDuplicateId == 1) {
                    $('.login_id_ok').css("display", "none");
                    $('.login_id_already').css("display", "inline-block");
                }
                else if (isDuplicateId == 0) {
                    $('.login_id_ok').css("display", "inline-block");
                    $('.login_id_already').css("display", "none");
                }
            },
            error : function () {
                alert("아이디 중복 확인 중 에러 발생");
            }
        });
    },
    checkPassword : function () {
        var data = {
            password1 : $('#password1').val(),
            password2 : $('#password2').val()
        };

        $.ajax({
            url:'/api/member/join/check/password',
            type : 'POST',
            data : data,
            success : function (isSamePW) {
                if (isSamePW == 1) {
                    $('.pw_same').css("display", "inline-block");
                    $('.pw_not_same').css("display", "none");
                }
                else if (isSamePW == 0) {
                    $('.pw_same').css("display", "none");
                    $('.pw_not_same').css("display", "inline-block");
                }
            },
            error : function () {
                alert("비밀번호 재확인 중 에러 발생");
            }
        })
    },
    login : function () {
        var data = {
            login_id: $('#login_id').val(),
            password: $('#password').val()
        };
        var pageNum = 1;
        var category_id = -1;

        //console.log("아이디 : ", data.login_id, "비밀번호 : ", data.password);
        $.ajax({
            url: '/api/member/login',
            type : 'POST',
            data : data,
            success: function (successLogin) {
                if (successLogin == 1) {
                    console.log("로그인 성공");
                    window.location.href = '/Board/postList/page=' + pageNum + '&category=' + category_id + '&query=';
                } else if (successLogin == 0) {
                    $('.error_message_login').css("display", "inline-block");
                    console.log("로그인 실패");
                }
            },
            error: function () {
                alert("로그인 중 에러 발생");
            }

        });
    },
    savePost : function () {

        var sCategory = $("select[name=category] > option:selected").val();
        var last_page = parseInt($('#last_page').val());
        var pageNum = 1;

        console.log("마지막 페이지 번호 :" + last_page);

        var data = {
            title: $('#title').val(),
            login_id: $('#login_id').val(),
            content: $('#content').val(),
            category_id : parseInt(sCategory),
            seq_num : $('#seq_num').val()
        };

        console.log("게시물 : ", data);
        
        $.ajax({
            type : 'POST',
            url : '/api/post/create',
            dataType : 'json',
            contentType : 'application/json; charset=utf-8',
            data : JSON.stringify(data)
        }).done(function () {
            alert('게시글을 등록하였습니다.');
            window.location.href = '/Board/postList/page=1' + '&category=-1&query=' + "";
        }).fail(function (error){
            alert(JSON.stringify(error));
        })
    },
    updatePost : function () {
        var sCategory = $("select[name=category] > option:selected").val();
        var currentPage = parseInt($('#currentPage').val());
        var search_keyword = $("#searchKeyword").val();

        var data = {
            post_id : parseInt($('#post_id').val()),
            title: $('#title').val(),
            content: $('#content').val(),
            category_id : parseInt(sCategory),
        };

        console.log("currentPage : %d", currentPage);

        $.ajax({
            type : 'PUT',
            url : '/api/post/update/' + data.post_id,
            dataType : 'json',
            contentType : 'application/json; charset=utf-8',
            data : JSON.stringify(data),
        }).done(function () {
            alert('수정이 완료되었습니다.');
            window.location.href = '/Board/post/read/' + data.post_id + '&page=' + currentPage + '&category=' + data.category_id + '&query=' + search_keyword;
        }).fail(function (error){
            alert(JSON.stringify(error));
        })
    },
    deletePost : function () {
        var post_id = $('#post_id').val();

        console.log("deletePost() => %d", post_id);

        $.ajax({
            type : 'DELETE',
            url : '/api/post/delete/' + post_id,
            dataType : 'json',
            contentType : 'application/json; charset=utf-8',
        }).done(function () {
            alert('삭제되었습니다.');
            window.location.href = '/Board/postList/page=1&category=-1&query= ';
        }).fail(function (error) {
            alert('삭제 중 오류가 발생하였습니다.');
        })
    },
    searchPost : function () {
         var search_keyword = $('#searchKeyword').val();
         var pageNum = 1;
         var category_id = $('#category.category_id').val();

         if (search_keyword != '') {
             $.ajax({
                 type : 'GET',
                 url : '/Board/postList/page=' + pageNum + '&category=' + category_id + '&query=' + search_keyword,
                 contentType : 'application/json; charset=utf-8',
                 success : function () {
                     window.location.href = '/Board/postList/page=' + pageNum + '&category=' + category_id + '&query=' + search_keyword;
                 }
             })
         }
         else
             window.location.href = '/Board/postList/page=1&category={category_id}&query=' + search_keyword;
    },
    saveComment : function () {
        var data = {
            content : $('#comment_content').val(),
            post_id : $('#post_id').val()
        }

        console.log(data);

        var page = $('#currentPage').val();

        $.ajax({
            type : 'POST',
            url : '/api/comment',
            dataType : 'json',
            contentType : 'application/json; charset=utf-8',
            data : JSON.stringify(data)
        }).done(function () {
            window.location.href = '/Board/post/read/' + data.post_id + '&page=' + page;
        }).fail(function () {
            alert('댓글 작성 중 오류 발생');
        })
    }
};

main.init();