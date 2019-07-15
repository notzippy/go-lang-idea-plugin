package main

type S struct {
	bb interface{}
	cc interface{}
	aa S
}

func main() {
	s := S{}
	s.aa = S{}
}