package main

type S struct {
	bb interface{}
	cc interface{}
}

func main() {
	s := S{}
	s.aa<caret> = S{}
}