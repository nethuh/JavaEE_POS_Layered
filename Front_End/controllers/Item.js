

getAllItems();

$("#itemGetAll-btn").click(function (){
    getAllItems();
}) ;

function getAllItems(){
    $("#tblItem").empty();
    $.ajax({
        url: baseUrl + "item?option=getAll",
        method : "GET",
        success:function (data){
            for (let i = 0; i < data.length; i++){
                let code = data[i].code;
                let  name = data[i].name;
                let price = data[i].Price;
                let qty = data[i].Qty;
                let row = `<tr><td>${code}</td><td>${name}</td><td>${price}</td><td>${qty}</td>`;
                $("#tblItem").append(row);

            }
    },
    });
}