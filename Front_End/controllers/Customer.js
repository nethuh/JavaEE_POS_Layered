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

$("#customerSave-btn").click(function () {
    let formData = $("#CustomerForm").serialize();
    let apiUrl = baseUrl + "customer"; // Make sure baseUrl is defined correctly
    $.ajax({
        url: apiUrl,
        method: "POST",
        data: formData,
        dataType: "json",
        success: function (res) {
            console.log("Success Method Invoked");
            console.log(res);
            alert(res.message);
            getAllCustomers();
        },
        error: function (xhr, status, error) {
            console.log("Error Method Invoked");
            console.log(xhr.responseText);
            alert("Error: " + error);
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
        cusID: cusId,
        cusName: cusName,
        cusAddress: cusAddress,
        cusSalary: cusSalary
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
