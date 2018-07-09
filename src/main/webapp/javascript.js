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

var sizecitieslist; //количество городов полученных из ответа на запрос в БД
var index = 0;//значение смещения количества элементов в запросе к БД
//var autoRow;//строка в которую помещаем таблицу completeTable


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
    var url = "ShowResultSearchAjaxServlet?action=requestComplete&id=" + escape(completeField.value) + "&index=" + index;
//        var url = "ShowResultSearchAjaxServlet?action=requestComplete&id=" + escape(completeField.value);
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
//        alert("req.readyState=4");
        if (req.status == 200) {// код ответа на наш запрос =запрос обработан успешно
//            alert("req.readyState=4, req.status=200");
            parseMessages(req.responseXML);//парсим полученное в ответ сообщение
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
//    linkElement.setAttribute("href", "autocomplete?action=lookup&id=");
    textelement.appendChild(document.createTextNode(name));
    cell.appendChild(textelement);

}
//для выравнивания таблицы предложений
function getElementY(element) {

    var targetTop = 0;

    if (element.offsetParent) {
        while (element.offsetParent) {
            targetTop += element.offsetTop;
            element = element.offsetParent;
        }
    } else if (element.y) {
        targetTop += element.y;
    }
    return targetTop;
}
//очистка таблицы предложений поиска
function clearTable() {
//    console.log("log:"+completeTable.getElementsByTagName("tr").length);
    if (completeTable.getElementsByTagName("tr").length > 0) {
//        completeTable.style.display = 'none';
        for (var item = completeTable.childNodes.length - 1; item >= 0; item--) {
            completeTable.removeChild(completeTable.childNodes[item]);
        }
    }
}
//парсинг xml ответа веб-сервера
function parseMessages(responseXML) {

    // no matches returned
    if (responseXML == null) {
        return false;
    } else {

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
            //создаем таблицу для вставки кнопок вперед назад и информации что данные отсутствуют
            console.log(rowPaging);
            if (rowPaging == null)               
            {
                rowPaging = document.createElement("tr");//создали строку
                cellNextPaging = document.createElement("td");
                cellBackPaging = document.createElement("td");
                cellInfoPaging = document.createElement("td");

                rowPaging.appendChild(cellBackPaging);
                rowPaging.appendChild(cellInfoPaging);
                rowPaging.appendChild(cellNextPaging);
                pagingTable.appendChild(rowPaging);
            }


//            var textButtonNext=document.createTextNode("Вперёд");

            if (sizecitieslist > 4) {
                addButtonNext();
            }
            if (index > 0) {
                addButtonBack();
            }

        }
        if ((index == 0) && (sizecitieslist <= 0)) {
            noData();
        }
    }
}

function addButtonNext() {
    //добавляем кнопку Вперед в правой части страницы
    var buttonNext = document.createElement("input");
    buttonNext.setAttribute("type", "button");
    buttonNext.setAttribute("value", "Вперёд");
    buttonNext.setAttribute("onclick", "clickButtonNext()");
    console.log(cellNextPaging.get);
    if (cellNextPaging.lastChild)cellNextPaging.replaceChild(buttonNext,buttonNext);
    else cellNextPaging.appendChild(buttonNext);
//    if (sizecitieslist<5) buttonNext.setAttribute("hidden","hidden");
//    else buttonNext.setAttribute("hidden","");

}
function clickButtonNext() {
    index = index + 5;
    doCompletion();
}

function addButtonBack() {
    //<input type="button" value="Поиск" onclick="clickButtonBack();">
    //добавляем кнопку Назад в левой части страницы
    var buttonBack = document.createElement("input");
    buttonBack.setAttribute("type", "button");
    buttonBack.setAttribute("value", "Назад");
    buttonBack.setAttribute("onclick", "clickButtonBack()");
    if (cellBackPaging.lastChild)cellBackPaging.replaceChild(buttonBack,buttonBack);
    else cellBackPaging.appendChild(buttonBack);
}
function clickButtonBack() {
    index = index - 5;
    doCompletion();
}

function noData() {
    //<a href=\"SearchCountryPaging\" > Повторить поиск </a>
    //добавляем фразу "БД нет данных по запросу"
}