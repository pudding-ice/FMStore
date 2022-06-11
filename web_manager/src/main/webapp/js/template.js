new Vue({
    el: "#app",
    data: {
        tempList: [],
        temp: {},
        searchTemp: '',
        current: 1,  //显示的是哪一页
        pageSize: 10, //每一页显示的数据条数
        total: 0, //记录总数
        maxPageIndex: 15,
        selectIds: [] //记录选择了哪些记录的id
    },
    methods: {
        pageHandler: function (current) {
            var _this = this;
            this.page = current;
            var request = {
                current: this.current,
                pageSize: this.pageSize,
                queryContent: this.searchTemp
            };
            axios.post("/temp/search.do", request)
                .then(function (response) {
                    let data = response.data;
                    //取服务端响应的结果
                    _this.tempList = data.rows;
                    _this.total = data.total;
                    console.log(data);
                }).catch(function (reason) {
                console.log(reason);
            })
        },// 定义方法：获取JSON字符串中的某个key对应值的集合
        jsonToString: function (jsonStr, key) {
            // 将字符串转成JSOn:
            var jsonObj = JSON.parse(jsonStr);
            var value = "";
            for (var i = 0; i < jsonObj.length; i++) {
                if (i > 0) {
                    value += ",";
                }
                value += jsonObj[i][key];
            }
            return value;
        },

    },
    created: function () {
        this.pageHandler(1);
    }
});