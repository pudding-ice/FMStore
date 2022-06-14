new Vue({
    el: "#app",
    data: {
        enterprise: {}
    },
    methods: {
        save: function () {
            var _this = this;
            console.log(_this.enterprise);
            _this.enterprise.status = 0;
            axios.post("/seller/add.do", _this.enterprise).then((res) => {
                let data = res.data;
                if (data.success) {
                    console.log(data.message);
                }
            })
        },
    }
});