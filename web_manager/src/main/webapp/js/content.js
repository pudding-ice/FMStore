new Vue({
    el: "#app",
    data: {
        current: 1,  //显示的是哪一页
        pageSize: 10, //每一页显示的数据条数
        total: 0, //记录总数
        maxPageIndex: 0,
        searchEntity: {
            title: ''
        },
        categoryList: [],
        contentList: [],
        selectIds: [], //记录选择了哪些记录的id,
        contentEntity: {
            id: '',
            categoryId: '',
            title: '',
            url: '',
            pic: '',
            status: '0',
            sortOrder: ''
        },
        status: {
            1: "有效",
            0: "无效"
        }
    },
    methods: {
        pageHandler: function (current) {
            let _this = this;
            this.page = current;
            let param = {
                current: this.current,
                pageSize: this.pageSize,
                queryContent: this.searchEntity
            }
            axios.post("/content/search.do?", param)
                .then((response) => {
                    //取服务端响应的结果
                    let data = response.data;
                    _this.contentList = data.rows;
                    _this.total = data.total;
                }).catch(function (reason) {
                console.log(reason);
            })
        },
        loadCategoryContent: function () {
            let _this = this;
            axios.post("/contentCategory/getAll.do")
                .then(function (response) {
                    //取服务端响应的结果
                    _this.categoryList = response.data;
                    // console.log(response.data);
                }).catch(function (reason) {
                console.log(reason);
            })
        },
        uploadFile: function () {
            let formData = new FormData();
            formData.append('file', file.files[0])
            const instance = axios.create({
                withCredentials: true
            });
            let _this = this;
            console.log(this.contentEntity);
            instance.post("/fdfs/upload.do", formData).then(function (response) {
                console.log(response.data);
                _this.contentEntity.pic = response.data.message;
                console.log(_this.contentList);
            }).catch(function (reason) {
                console.log(reason);
            });
        },
        saveContent: function () {
            let _this = this;
            console.log(this.contentEntity)
            axios.post("/content/add.do", _this.contentEntity)
                .then((res) => {
                    //取服务端响应的结果
                    let data = res.data;
                    if (data.success) {
                        alert(data.message)
                        window.location.reload();
                    } else {
                        alert(data.message);
                    }
                }).catch(function (reason) {
                console.log(reason);
            })
        }
    },
    created: function () {
        this.pageHandler(1);
        this.loadCategoryContent();
    },
});