/*

    The router for all /list/* pathes


    NOTE:  Order of pathes IMPORTANT
    Express will try to match top down
    If something before matches, .... then what should match NOT executed.
*/

var express = require('express');
var TodoNote = require('../models/todonote.js');
var TodoList = require('../models/todolist.js');
var userRegistry = require('../models/userregistry.js');

var todolist = new TodoList();
var router = express.Router();

router.use('/', function (req, res, next) {
	if (req.session.user) {
		next();
	}
	else {
		res.redirect("/todo/login")
	}
});

// List notes for current user
router.get('/', function(req, res, next) {
	console.log(req.session.user);
	var data = todolist.getNotes();
    res.render('list', {text:'List of all ToDo:s', data: data, user: req.session.user});
});

router.get('/add', function(req, res, next) {
	res.render('add', {text:'Add new post', user: req.session.user});
});

// TODO get by id

router.post('/add', function(req, res, next) {
	var addNote = new TodoNote(null,req.body.text);
	todolist.add(addNote);
	res.redirect("/todo/list");
});

router.get('/delete', function(req, res, next) {
	var tmpNote = todolist.getById(parseInt(req.query.id));
    res.render('delete', {text:'List of all ToDo:s', note: tmpNote, user:req.session.user});
});

router.post('/delete', function(req, res, next) {
	todolist.delete(parseInt(req.body.id));
	res.redirect("/todo/list")
});

// TODO edit by id
router.get('/edit', function(req, res, next) {
	var tmpNote = todolist.getById(parseInt(req.query.id));
    res.render('edit', {text:'List of all ToDo:s', note: tmpNote, user:req.session.user});
});
router.post('/edit', function(req, res, next) {
	var editNote = new TodoNote(parseInt(req.body.id),req.body.text,null,req.body.done);
	todolist.update(editNote);
    res.redirect("/todo/list")
});

module.exports = router;
