new Vue({
    el: "#app",
    data: {
        brandList: [],
        current: 1,
        pageSize: 10,
        total: 100,
        maxPageIndex: 15
    },
    methods: {
        getAllBrands: function () {
            //如果在axios的get方法中使用this关键字,代表的是axios对象,而不是Vue对象,
            //所以要在方法外定义一个变量来代表当前Vue对象
            var _this = this;
            axios.get("/brand/getAll.do").then((response) => {
                this.brandList = response.data;
            }).catch()
        },
        pageHandler: function (current) {
            this.current = current
        }
    },
    created: function () {
        this.getAllBrands()
    }

})