let baseUrl ="http://localhost:8081/posApp/";

getAllCustomers();

$("#customerGetAll-btn").click(function (){
    getAllCustomers();
});

function getAllCustomers(){
    $("#tblCustomer").empty();
    $.ajax({
        url : baseUrl + "customer",
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

//delete customer
$("#customerDelete-btn").click(function () {
    let id = $("#txtCustID").val();
    $.ajax({
        url: baseUrl+"customer?cusID=" + id,
        method: "delete",
        success: function (resp) {
            getAllCustomers();
            alert(resp.message);
        },
        error: function (error) {
            let message = JSON.parse(error.responseText).message;
            alert(message);
        }
    });
});

//update customer
$("#customerUpdated-btn").click(function () {
    let cusId = $('#txtCustID').val();
    let cusName = $('#txtcusName').val();
    let cusAddress = $('#txtcusSalary').val();
    let cusSalary = $('#txtcusAddress').val();
    var customerOb = {
        id: cusId,
        name: cusName,
        address: cusAddress,
        salary: cusSalary
    }
    $.ajax({
        url: baseUrl+"customer",
        method: "put",
        contentType: "application/json",
        data: JSON.stringify(customerOb),
        dataType: "json",
        success: function (resp) {
            getAllCustomers();
            alert(resp.message);
        },
        error: function (error) {
            let message = JSON.parse(error.responseText).message;
            alert(message);
        }
    });
});
