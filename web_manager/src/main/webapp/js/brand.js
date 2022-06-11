
new Vue({
    el: "#app",
    data: {
        brandList: [],
        current: 1,
        pageSize: 10,
        total: 100,
        maxPageIndex: 15,
        brand: {
            id: null,
            name: '',
            firstChar: ''
        },
        selectedId: []
    },
    methods: {
        getAllBrands: function () {
            //如果在axios的get方法中使用this关键字,代表的是axios对象,而不是Vue对象,
            //所以要在方法外定义一个变量来代表当前Vue对象
            var _this = this;
            axios.get("/brand/getAll.do").then((response) => {
                _this.brandList = response.data;
            }).catch()
        },
        pageHandler: function (current) {
            //设置当前页码为点击的页码
            this.current = current
            var _this = this;
            //rest风格
            axios.get("/brand/getPage/" + current + "/" + _this.pageSize + ".do").then((response) => {
                var data = response.data;
                _this.brandList = data.rows;
                _this.total = data.total;
            }).catch(function (reason) {
                console.log(reason)
            })
        },
        save: function () {
            var _this = this;
            axios.post("/brand/save.do", _this.brand).then((res) => {
                var data = res.data;
                if (data.success) {
                    _this.pageHandler(1);
                } else {
                    console.log(data.message)
                }
            }).catch(function (reason) {
                console.log(reason)
            })
        },
        flushData: function () {
            this.brand.name = '';
            this.brand.firstChar = '';
        },
        findOneById: function (id) {
            console.log("来到find方法")
            this.brandList.forEach((e) => {
                if (id === e.id) {
                    this.brand.id = e.id;
                    this.brand.name = e.name;
                    this.brand.firstChar = e.firstChar;
                }
            })
        },
        handleSelected: function (event, id) {
            if (event.target.checked) {
                //选中
                this.selectedId.push(id);
            } else {
                //取消选中
                let idx = this.selectedId.indexOf(id);
                this.selectedId.splice(idx, 1)
            }
        },
        deleteBrand: function () {
            var _this = this;
            Qs.stringify()
            let param = Qs.stringify({ids: _this.selectedId}, {indices: false});
            console.log(param);
            axios.post("/brand/delete.do", param).then((res) => {
                let data = res.data;
                if (data.success) {
                    _this.pageHandler(1);
                    alert(data.message);
                } else {
                    alert(data.message);
                }
            })
        }
    },
    created: function () {
        this.pageHandler(1);
    }

})