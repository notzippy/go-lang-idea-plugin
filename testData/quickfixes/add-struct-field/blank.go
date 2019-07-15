package main

type S struct {
	bb interface{}
	cc interface{}
}

func main() {
	s := S{}
	s.<error descr="Unresolved reference '_'">_<caret></error> = "aa"
}