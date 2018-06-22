/*
 * Use and copying for commercial purposes
 * only with the author's permission
 */

var req;
var isIE;
var completeField;//поле ввода символов
var completeTable;//таблица, выводящая в ответ список стран
//var autoRow;//строка в которую помещаем таблицу completeTable

function init() {
    completeField = document.getElementById("country");//возвращает из 
    //документа ссылку на элемент, который имеет атрибут id с указанным 
    //значением. В нашем случае это поле ввода текса
    completeTable = document.getElementById("complete-table");
//    autoRow = document.getElementById("auto-row");
    completeTable.style.top = getElementY(completeTable) + "px";//выравнивание таблицы
}
//запрос на веб-сервер
function doCompletion() {
        //формируем строку для запроса к серверу
        var url = "autocomplete?action=complete&id=" + escape(completeField.value);
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
    }
}
//добавляет страну в таблицу предложений поиска
function appendCountry(name) {

    var row;
    var cell;
    var linkElement;

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

    linkElement = document.createElement("a");
    linkElement.className = "popupItem";
    linkElement.setAttribute("href", "autocomplete?action=lookup&id=" );
    linkElement.appendChild(document.createTextNode(name));
    cell.appendChild(linkElement);
}
//для выравнивания таблицы предложений
function getElementY(element){

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
    if (completeTable.getElementsByTagName("tr").length > 0) {
        completeTable.style.display = 'none';
        for (loop = completeTable.childNodes.length -1; loop >= 0 ; loop--) {
            completeTable.removeChild(completeTable.childNodes[loop]);
        }
    }
}
//парсинг ответа веб-сервера
function parseMessages(responseXML) {

    // no matches returned
    if (responseXML == null) {
        return false;
    } else {
        
        //присваиваем перый элемент массива ссылок данного имени, 
        //найденных во всем документе
        var countries = responseXML.getElementsByTagName("countries")[0];
        
        if (countries.childNodes.length > 0) {
            completeTable.setAttribute("bordercolor", "black");
            completeTable.setAttribute("border", "1");

            for (loop = 0; loop < countries.childNodes.length; loop++) {
                var country = countries.childNodes[loop];
                var name = country.getElementsByTagName("name")[0];
//                var lastName = country.getElementsByTagName("lastName")[0];
//                var composerId = country.getElementsByTagName("id")[0];
                appendCountry(name.childNodes[0].nodeValue);
            }
        }
    }
}