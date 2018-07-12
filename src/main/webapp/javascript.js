/*
 * Use and copying for commercial purposes
 * only with the author's permission
 */

var req;
var isIE;
var completeField;//поле ввода символов
var completeTable;//таблица, выводящая в ответ список стран

var pagingTable;//таблица для отрисовывания кнопок вперед назад и фразы что запрашиваемых данных нет
var rowPaging = null;//создали строку
var cellNextPaging;//ячейка для кнопки Вперёд
var cellBackPaging;//ячейка для кнопки Назад
var cellInfoPaging;//ячейка для вывода информации
var replase;//утверждение о том что создана таблица для пейджинга 
var sizecitieslist=0; //количество городов полученных из ответа на запрос в БД
var index = 0;//значение смещения количества элементов в запросе к БД
//function init() {//нужно попробовать сделать разовой данную функцию а не регулярно выполняющейся
//    completeField = document.getElementById("country");//возвращает из 
//    //документа ссылку на элемент, который имеет атрибут id с указанным 
//    //значением. В нашем случае это поле ввода текса
//    completeIndex=document.getElementById("hiddenIndex");//возвращает из запроса
//    // ссылку на элемент hiddenIndex (для пролистывания формируемого списка городов)
//    completeTable = document.getElementById("complete-table");
////    autoRow = document.getElementById("auto-row");
////    completeTable.style.top = getElementY(completeTable) + "px";//выравнивание таблицы
//}

//запрос на веб-сервер
function doCompletion() {
    completeField = document.getElementById("country");//возвращает из 
    //документа ссылку на элемент, который имеет атрибут id с указанным 
    //значением. В нашем случае это поле ввода текса
    completeIndex = document.getElementById("hiddenIndex");//возвращает из запроса
    // ссылку на элемент hiddenIndex (для пролистывания формируемого списка городов)
    completeTable = document.getElementById("complete-table");
//    autoRow = document.getElementById("auto-row");
//    completeTable.style.top = getElementY(completeTable) + "px";//выравнивание таблицы
    pagingTable = document.getElementById("paging-table");

    /**
     * формируем строку для GET запроса к серверу через сервлет         * 
     * Url состоит из:
     * - имени сервлета к которому посылаем запрос
     * - передаваемого параметра  action (используется при идентификации 
     * запроса сервлетом)
     * - передаваемого параметра id
     * - передаваемого параметра index
     */
    var url = "ShowResultSearchAjaxServlet?action=requestComplete&id=" + encodeURIComponent(completeField.value) + "&index=" + index;
//    при формировании запроса русские буквы подготавливаем с помощью encodeURIComponent()
      
    //создаем объект запроса
    req = initRequest();//метод вызывающий метод (который возвращает объект
    // XMLHttpRequest или ActiveXObject)
    // 
    //конфигурируем объект запроса
    req.open("GET", url, true);//тип запроса, строка адреса, асинхронный запрос
    /*
     * Если взаимодействие определено как асинхронное, необходимо указать 
     * функцию обратного вызова. Функция обратного вызова для этого 
     * взаимодействия определяется при помощи следующего оператора:        
     */
    req.onreadystatechange = callback; //функция обратного вызова callback,
    // которая будет вызываться при изменении состояния readyState 
    // объекта XMLHttpRequest нашего подготовленного запроса в процессе 
    // его дальнейшей отправки    

    req.send(null);//отсылаем запрос
}
// подготовка запроса для адаптации с браузером
function initRequest() {
    if (window.XMLHttpRequest) {//если возможно вызвать метод XMLHttpRequest
        //проверка на используемый браузер (MSIE)
        if (navigator.userAgent.indexOf('MSIE') != -1) {
            isIE = true;
        }
        return new XMLHttpRequest();//то вызвваем его

    } else if (window.ActiveXObject) {//иначе пробуем вызвать метод для MSIE
        isIE = true;
        return new ActiveXObject("Microsoft.XMLHTTP");//и делаем это если это возможно 
    }
}
//функция обратного вызова, запускающая обработчик принятого ответа от веб-сервера
function callback() {

    clearTable();//любые скомбинированные записи, существующие в окне 
    //автозавершения, удаляются до того, как выполняется заполнение новыми записями.
    
    if (req.readyState == 4) {//состояние объекта XMLHttpRequest нашего запроса=запрос завершен и ответ готов 
        if (req.status == 200) {// код ответа на наш запрос =запрос обработан успешно
            parseMessages(req.responseXML);//парсим полученное в ответ сообщение
        }
        if(req.status==204) {//если ответ не содержит данных(пустой)
            parseMessages(req.responseXML);//можно подумать над тем что сделать отдельную функцию для пустого ответа
        }
    }
}

//добавляет страну в таблицу вывода
function appendCity(name) {
    var row;
    var cell;
    var textelement;

    if (isIE) {
        completeTable.style.display = 'block';
        row = completeTable.insertRow(completeTable.rows.length);
        cell = row.insertCell(0);
    } else {
        completeTable.style.display = 'table';
        row = document.createElement("tr");
        cell = document.createElement("td");
        row.appendChild(cell);
        completeTable.appendChild(row);
    }
    cell.className = "popupCell";
    textelement = document.createElement("h7");
    textelement.className = "popupItem";
    textelement.appendChild(document.createTextNode(name));
    cell.appendChild(textelement);
}

//очистка таблицы предложений поиска
function clearTable() {
    if (completeTable.getElementsByTagName("tr").length > 0) {//проверка на наличие элементов
        for (var item = completeTable.childNodes.length - 1; item >= 0; item--) {
            completeTable.removeChild(completeTable.childNodes[item]);//удаление поэлементное
        }
    }
    if (pagingTable.getElementsByTagName("td").length>0){//проверка на наличие элементов
        for (var item=pagingTable.childNodes.length-1;item>=0;item--){
            pagingTable.removeChild(pagingTable.childNodes[item]);
        }
        rowPaging=null;//для удаления строки в таблице pagingTable
    }
}
//парсинг xml ответа веб-сервера
function parseMessages(responseXML) {
console.log(responseXML);
//создаем таблицу для вставки кнопок вперед назад и информации что данные отсутствуют
//            console.log(rowPaging);
            if (rowPaging == null)
            {
                rowPaging = document.createElement("tr");//создали строку
                cellNextPaging = document.createElement("td");
                cellNextPaging.id = "cellNextPaging";
                cellBackPaging = document.createElement("td");
                cellInfoPaging = document.createElement("td");

                rowPaging.appendChild(cellBackPaging);
                rowPaging.appendChild(cellInfoPaging);
                rowPaging.appendChild(cellNextPaging);
                pagingTable.appendChild(rowPaging);
            }
    
    if (responseXML != null) {

        //присваиваем первый элемент массива ссылок данного имени, 
        //найденных во всем xml документе
        var cities = responseXML.getElementsByTagName("cities")[0];
        sizecitieslist = cities.childNodes.length;
        if (cities.childNodes.length > 0) {
//            console.log(cities.childNodes.length);
            completeTable.setAttribute("bordercolor", "black");
            completeTable.setAttribute("border", "1");
            completeTable.setAttribute("cellspacing", "0");
//            alert("response added");

//            console.log("size="+sizecitieslist);

            for (var item = 0; item < (cities.childNodes.length); item++) {
                var city = cities.childNodes[item];
                var name = city.getElementsByTagName("name")[0];//хранит ссылку на элемент name

                //добавляем в таблицу html страницы распаресенные данные
//                alert("TEST");
//                console.log(cities.childNodes.length);
//                console.log(name.childNodes[0].nodeValue);
                appendCity(name.childNodes[0].nodeValue);
//                console.log(1000);
            }
            
//            var textButtonNext=document.createTextNode("Вперёд");

            if (sizecitieslist > 4) addButtonNext();
            else deleteButtonNext();
            console.log("index="+index);
            
            if (index > 0)addButtonBack();
            else deleteButtonBack();

        }
//        if ((index == 0) && (sizecitieslist <= 0)) {
//            console.log("TEST1111");
//            addNoData();
//        }
    }
    else{
        console.log("111111");
            addNoData();
    }
}

function addButtonNext() {
    //добавляем кнопку Вперед в правой части страницы
    var buttonNext = document.createElement("input");
    buttonNext.id = "buttonNext";
    buttonNext.setAttribute("type", "button");
    buttonNext.setAttribute("value", "Вперёд");
    buttonNext.setAttribute("onclick", "clickButtonNext()");
//    console.log(cellNextPaging.get);
    if (cellNextPaging.lastChild){
        var oldbutton=document.getElementById("buttonNext");
        cellNextPaging.replaceChild(buttonNext, oldbutton);
    }
    else
        cellNextPaging.appendChild(buttonNext);
//    console.log("sizecitieslist=" + sizecitieslist);
}
function deleteButtonNext() {
//    console.log("sizecitieslist=" + sizecitieslist);
    if (cellNextPaging.lastChild){
        var removebutton=document.getElementById("buttonNext");
        cellNextPaging.removeChild(removebutton); 
    }  
}
function clickButtonNext() {
    index = index + 5;
    doCompletion();
}

function addButtonBack() { 
    //добавляем кнопку Назад в левой части страницы
    var buttonBack = document.createElement("input");
    buttonBack.id="buttonBack";
    buttonBack.setAttribute("type", "button");
    buttonBack.setAttribute("value", "Назад");
    buttonBack.setAttribute("onclick", "clickButtonBack()");
    if (cellBackPaging.lastChild)
        cellBackPaging.replaceChild(buttonBack, buttonBack);
    else
        cellBackPaging.appendChild(buttonBack);
}

function deleteButtonBack(){
    if (cellBackPaging.lastChild){
        var removebutton=document.getElementById("buttonBack");
        cellBackPaging.removeChild(removebutton); 
    }
}

function clickButtonBack() {
    index = index - 5;
    doCompletion();
}

function addNoData() {
    console.log("sizesl="+sizecitieslist);
    var elementNoData=document.createElement("h4");
    var textNoData=document.createTextNode("В базе нет данных по вашему запросу. Измените запрос.");
    //<a href=\"SearchCountryPaging\" > Повторить поиск </a> if (cellNextPaging.lastChild){
      if (cellInfoPaging.lastChild){
//        var oldbutton=document.getElementById("buttonNext");
//        cellInfoPaging.replaceChild(buttonNext, oldbutton);
    }
    else
        cellInfoPaging.appendChild(elementNoData.appendChild(textNoData));
    //добавляем фразу "БД нет данных по запросу"
}