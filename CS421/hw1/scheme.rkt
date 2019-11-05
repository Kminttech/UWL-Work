#lang racket

(define (partition L N)
  (if (null? L) '() (cons (prefix L N) (partition (removeFirst L N) N))))

(define (removeFirst L N)
  (if (or (<= N 0) (null? L)) L (removeFirst (cdr L) (- N 1))))

(define (cycle ALIST N)
  (cons ALIST (cycle ALIST (- N 1))))

(define (list-replace ALIST SYM VAL)
  (cond ((null? ALIST) '())
        ((list? (car ALIST)) (cons (list-replace (car ALIST) SYM VAL) (list-replace (cdr ALIST) SYM VAL)))
        ((eq? (car ALIST) SYM) (cons VAL (list-replace (cdr ALIST) SYM VAL)))
        (else (cons (car ALIST) (list-replace (cdr ALIST) SYM VAL)))))

(define (repeat VAL COUNT)
  (if (> COUNT 0) (cons VAL (repeat VAL (- COUNT 1))) '()))

(define (summer L)
  (if (null? L) '() (summerHelp L 0)))

(define (summerHelp L N)
  (if (null? L) '() (cons (+ N (car L)) (summerHelp (cdr L) (+ N (car L))))))

(define (counts XS)
  (if (null? XS) '() (countsRecurse XS '())))

(define (countsRecurse XS L)
  (cond ((null? XS) L)
        (else (countsRecurse (cdr XS) (traverseIncrement L (car XS))))))

(define (traverseIncrement L VAL)
  (cond ((null? L) (list (list VAL 1)))
        ((eq? (caar L) VAL) (cons (list VAL (+ (cadar L) 1)) (cdr L)))
        (else (cons (car L) (traverseIncrement (cdr L) VAL)))))

(define (prefix L N)
  (if (or (<= N 0) (null? L)) '() (cons (car L) (prefix (cdr L) (- N 1)))))

(define (el-graph->x-graph g)
  (if (null? g) '() (elxRecurse g '())))

(define (elxRecurse g L)
  (cond ((null? g) (checkELX g L))
        ((= (length g) 1) (traverseUpdate L (caar g) (cadar g)))
        (else (elxRecurse (cdr g) (traverseUpdate L (caar g) (cadar g))))))

(define (traverseUpdate L VAL ADD)
  (cond ((null? L) (list (list VAL (list ADD))))
        ((eq? (caar L) VAL) (cons (list VAL (append (cadar L) (list ADD))) (cdr L)))
        (else (cons (car L) (traverseUpdate (cdr L) VAL ADD)))))

(define (checkELX g L)
  '())

(define (x-graph->el-graph g)
  (if (null? g) '() (append (xelHelper (caar g) (cadar g)) (x-graph->el-graph (cdr g)))))

(define (xelHelper VAL L)
  (if (null? L) '() (cons (list VAL (car L)) (xelHelper VAL (cdr L)))))

(define (evaluate exp)
  (eval (list-replace (list-replace (list-replace exp 'block 'let) '% 'mod) '^ 'expt)))

(provide evaluate el-graph->x-graph x-graph->el-graph prefix counts summer repeat list-replace cycle partition)