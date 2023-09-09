let baseUrl ="http://localhost:8081/posApp/";

getAllCustomers();

$("#customerGetAll-btn").click(function (){
    getAllCustomers();
});

function getAllCustomers(){
    $("#tblCustomer").empty();
    $.ajax({
        url : baseUrl + "customer?option=GetAll",
        method:"GET",
        success: function (cus){
            for (let i = 0; i < cus.length; i++){
                let id = cus[i].id;
                let name = cus[i].name;
                let address = cus[i].address;
                let salary = cus[i].salary;
                let row = `<tr><td>${id}</td><td>${name}</td><td>${address}</td><td>${salary}</td>`;
                $("#tblCustomer").append(row);
            }
        },
    });
}

$("#customerSave-btn").click(function (){
    let  formData = $("#CustomerForm").serialize();
    $.ajax({
        url: baseUrl+"customer",
        method: "post",
        data: formData,
        dataType: "json",
        success:function (res){
            console.log("success Method Invoked");
            console.log(res);
            alert(res.message);
            getAllCustomers();
        },
        error: function (error) {
            console.log("Error Method Invoked");
            console.log(JSON.parse(error.responseText));
            alert(JSON.parse(error.responseText).message);
        }
    });
});
