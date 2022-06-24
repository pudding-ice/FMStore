
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
            firstChar: '',
            logoUrl: ''
        },
        selectedId: [],
        searchBrand: {
            name: '',
            firstChar: ''
        },
        filePath: ''

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
            var queryContent = this.searchBrand;
            console.log(queryContent)
            //rest风格
            axios.post("/brand/getPage/" + current + "/" + _this.pageSize + ".do", queryContent).then((response) => {
                var data = response.data;
                _this.brandList = data.rows;
                _this.total = data.total;
            }).catch(function (reason) {
                console.log(reason)
            })
        },
        uploadLogoImg: function () {
            let formData = new FormData();
            formData.append('file', photoFile.files[0])
            const instance = axios.create({
                withCredentials: true
            });
            let _this = this;
            instance.post("/fdfs/upload.do", formData).then(function (response) {
                _this.brand.logoUrl = response.data.message;
                _this.save();
            }).catch(function (reason) {
                console.log(reason);
            });
        },
        save: function () {
            var _this = this;
            axios.post("/brand/save.do", _this.brand).then((res) => {
                var data = res.data;
                if (data.success) {
                    _this.pageHandler(1);
                    alert("保存品牌成功");
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
            let _this = this;
            this.brandList.forEach((e) => {
                if (id === e.id) {
                    _this.brand.id = e.id;
                    _this.brand.name = e.name;
                    _this.brand.firstChar = e.firstChar;
                    _this.brand.logoUrl = e.logoUrl;
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
            if (this.selectedId.length == 0) {
                alert("至少选中一行删除!");
                return;
            }
            var _this = this;
            let param = Qs.stringify({ids: _this.selectedId}, {indices: false});
            axios.post("/brand/delete.do", param).then((res) => {
                let data = res.data;
                if (data.success) {
                    window.location.reload();
                    alert(data.message);
                } else {
                    alert(data.message);
                }
            })
        },
        upload(event) {
            let files = event.target.files[0];
            this.brand.logoUrl = this.getObjectUrl(files);
        },
        getObjectUrl(file) {
            let url = null;
            if (window.createObjectURL != undefined) {
                // basic
                url = window.createObjectURL(file);
            } else if (window.webkitURL != undefined) {
                // webkit or chrome
                url = window.webkitURL.createObjectURL(file);
            } else if (window.URL != undefined) {
                // mozilla(firefox)
                url = window.URL.createObjectURL(file);
            }
            return url;
        }
    },
    created: function () {
        this.pageHandler(1);
    }

})
