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
            specOpts: []
        },
        selectedId: [],
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
            this.spec.id = null;
            this.spec.specName = '';
            this.OptionList.specOpts = [];
        },
        save: function () {
            var _this = this;
            var parmas = {
                spec: _this.spec,
                specOpts: this.OptionList.specOpts
            }
            axios.post("/spec/save.do", parmas).then((res) => {
                var data = res.data;
                if (data.success) {
                    alert(data.message);
                    _this.pageHandler(1);
                } else {
                    alert(data.message)
                }
            }).catch(function (reason) {
                console.log(reason)
            })
        },
        getOptsById: function (id) {
            var _this = this;
            axios.get("/spec/getOpsById/" + id + ".do").then((res) => {
                let data = res.data;
                _this.spec = data.spec;
                _this.OptionList.specOpts = data.specOpts;
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
            axios.post("/spec/delete.do", param).then((res) => {
                let data = res.data;
                if (data.success) {
                    window.location.reload();
                    alert(data.message);
                } else {
                    alert(data.message);
                }
            })
        },
    },
    created: function () {
        this.pageHandler(1);
    }

})