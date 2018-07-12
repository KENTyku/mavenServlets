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
var sizecitieslist = 0; //количество городов полученных из ответа на запрос в БД
var index = 0;//значение смещения количества элементов в запросе к БД
//
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
        if (req.status == 204) {//если ответ не содержит данных(пустой)
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
    if (pagingTable.getElementsByTagName("td").length > 0) {//проверка на наличие элементов
        for (var item = pagingTable.childNodes.length - 1; item >= 0; item--) {
            pagingTable.removeChild(pagingTable.childNodes[item]);
        }
        rowPaging = null;//для удаления строки в таблице pagingTable
    }
}
//парсинг xml ответа веб-сервера
function parseMessages(responseXML) {
//создаем таблицу для вставки кнопок вперед назад и информации что данные отсутствуют
    if (rowPaging == null)//если строка в таблице pagingTable не существует, 
    //значит там нет еще ячеек и нужно их добавить
    {
        //определили элементы страницы
        rowPaging = document.createElement("tr");//создали строку
        cellNextPaging = document.createElement("td");//создали столбец(ячейку)
        cellNextPaging.id = "cellNextPaging";//присвоили ячейке id
        cellBackPaging = document.createElement("td");
        cellInfoPaging = document.createElement("td");
        //добавили элементы на страницу
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
            //настраиваем таблицу
            completeTable.setAttribute("bordercolor", "black");
            completeTable.setAttribute("border", "1");
            completeTable.setAttribute("cellspacing", "0");
            
            for (var item = 0; item < (cities.childNodes.length); item++) {
                var city = cities.childNodes[item];
                var name = city.getElementsByTagName("name")[0];//хранит ссылку на элемент name
                //добавляем в таблицу html страницы распаресенные данные
                appendCity(name.childNodes[0].nodeValue);
            }

            //условия добавления и удаления кнопки Вперед
            if (sizecitieslist > 4) addButtonNext();
            else deleteButtonNext();
            //условия добавления и удаления кнопки Назад
            if (index > 0)  addButtonBack();
            else deleteButtonBack();
        }

    } else {//условие вывода информации об отсутсвии данных в БД
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
    //проверка на наличие кнопки Вперед для ее замены или удаления
    if (cellNextPaging.lastChild) {
        var oldbutton = document.getElementById("buttonNext");
        cellNextPaging.replaceChild(buttonNext, oldbutton);
    } else cellNextPaging.appendChild(buttonNext);
}
//удаление кнопки Вперед
function deleteButtonNext() {
    if (cellNextPaging.lastChild) {
        var removebutton = document.getElementById("buttonNext");
        cellNextPaging.removeChild(removebutton);
    }
}

//обработка нажатия кнопки Вперед
function clickButtonNext() {
    index = index + 5;
    doCompletion();
}

function addButtonBack() {
    //добавляем кнопку Назад в левой части страницы
    var buttonBack = document.createElement("input");
    buttonBack.id = "buttonBack";
    buttonBack.setAttribute("type", "button");
    buttonBack.setAttribute("value", "Назад");
    buttonBack.setAttribute("onclick", "clickButtonBack()");
      //проверка на наличие кнопки Назад для ее замены или удаления
    if (cellBackPaging.lastChild)cellBackPaging.replaceChild(buttonBack, buttonBack);
    else cellBackPaging.appendChild(buttonBack);
}
//удаление кнопки Назад
function deleteButtonBack() {
    if (cellBackPaging.lastChild) {
        var removebutton = document.getElementById("buttonBack");
        cellBackPaging.removeChild(removebutton);
    }
}
//обработка нажатия кнопки Назад
function clickButtonBack() {
    index = index - 5;
    doCompletion();
}
//добавление сообщения об отсутствии данных в базе
function addNoData() {
    console.log("sizesl=" + sizecitieslist);
    var elementNoData = document.createElement("h4");
    var textNoData = document.createTextNode("В базе нет данных по вашему запросу. Измените запрос.");
    if (cellInfoPaging.lastChild) {
    } else cellInfoPaging.appendChild(elementNoData.appendChild(textNoData));
}