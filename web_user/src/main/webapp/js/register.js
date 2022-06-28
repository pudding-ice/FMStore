new Vue({
    el: "#app",
    data: {
        userEntity: {
            username: '',
            password: '',
            phone: '',
        },
        smscode: '',
        password2: ''
    },
    methods: {
        sendCode: function () {
            if (this.userEntity.phone == null || this.userEntity.phone === "") {
                alert("请填写手机号码");
                return;
            }
            axios.get('/user/sendCode/' + this.userEntity.phone + '.do')
                .then(function (response) {
                    //获取服务端响应的结果
                    console.log(response.data);
                    alert(response.data.message);
                }).catch(function (reason) {
                console.log(reason);
            });
        },
        register: function () {
            if (this.userEntity.phone == null || this.userEntity.phone == "") {
                alert("请填写手机号码");
                return;
            }
            //比较两次输入的密码是否一致
            if (this.userEntity.password != this.password2) {
                alert("两次输入密码不一致，请重新输入");
                this.userEntity.password = "";
                this.password2 = "";
                return;
            }
            //验证没有问题.发送请求给后端
            this.addUser();
        },
        addUser: function () {
            let _this = this;
            axios.post('/user/add/' + _this.smscode + '.do', _this.userEntity).then((res) => {
                let data = res.data;
                if (data.success) {
                    alert(data.message);
                    window.location.href = '/login.html';
                } else {
                    alert(data.message);
                }
            })
        }
    },
    created: function () {

    }
});