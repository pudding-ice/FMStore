new Vue({
    el: "#app",
    data: {
        specList: [],
        current: 1,
        pageSize: 10,
        total: 0,
        maxPageIndex: 15,
        queryName: '',
        spec: {
            id: null,
            specName: '',
        },
        OptionList: {
            specName: null,
            specOpts: []
        },
        // selectedId: [],
        // searchBrand: {
        //     name: '',
        //     firstChar: ''
        // }
    },
    methods: {
        pageHandler: function (current) {
            //设置当前页码为点击的页码
            this.current = current
            var _this = this;
            //rest风格
            axios.post("/spec/getSpecificationPage/" + current + "/" + _this.pageSize + "/" + _this.queryName + ".do").then((response) => {
                var data = response.data;
                _this.specList = data.rows;
                _this.total = data.total;
            }).catch(function (reason) {
                console.log(reason)
            })
        },
        addSpecOption: function () {
            var specOpts = this.OptionList.specOpts
            specOpts.push({
                id: null,
                optionName: '',
                orders: null
            })
        },
        deleteOption: function (index) {
            var specOpts = this.OptionList.specOpts;
            specOpts.splice(index, 1);
        },
        reloadOptionForm: function () {
            this.OptionList.specOpts = [];
        },
        save: function () {
            var _this = this;
            // console.log(this.OptionList.specOpts);
            var parmas = {
                spec: _this.spec,
                specOpts: this.OptionList.specOpts
            }
            axios.post("/spec/save.do", parmas).then((res) => {
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
        // flushData: function () {
        //     this.brand.name = '';
        //     this.brand.firstChar = '';
        // },
        // findOneById: function (id) {
        //     console.log("来到find方法")
        //     this.brandList.forEach((e) => {
        //         if (id === e.id) {
        //             this.brand.id = e.id;
        //             this.brand.name = e.name;
        //             this.brand.firstChar = e.firstChar;
        //         }
        //     })
        // },
        // handleSelected: function (event, id) {
        //     if (event.target.checked) {
        //         //选中
        //         this.selectedId.push(id);
        //     } else {
        //         //取消选中
        //         let idx = this.selectedId.indexOf(id);
        //         this.selectedId.splice(idx, 1)
        //     }
        // },
        // deleteBrand: function () {
        //     if (this.selectedId.length == 0) {
        //         alert("至少选中一行删除!");
        //         return;
        //     }
        //     var _this = this;
        //     Qs.stringify()
        //     let param = Qs.stringify({ids: _this.selectedId}, {indices: false});
        //     axios.post("/brand/delete.do", param).then((res) => {
        //         let data = res.data;
        //         if (data.success) {
        //             window.location.reload();
        //             alert(data.message);
        //         } else {
        //             alert(data.message);
        //         }
        //     })
        // }
    },
    created: function () {
        this.pageHandler(1);
    }

})