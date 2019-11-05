#lang racket

(define (heap-create)
  (list '() '() '()))

(define (heap-is-empty? h)
  (if (null? (car h)) #t #f))

(define (heap-size h)
  (cond ((null? h) 0)
        (else (+ 1 (heap-size (cadr h)) (heap-size (caddr h))))))

(define (heap-insert h f x)
  (cond ((heap-is-empty? h) (cons x (cdr h)))
        (else (heap-insert-help h f x (heap-path (+ (heap-size h) 1) '())))))

(define (heap-insert-help h f x PATH)
  (cond ((null? h) (list x '() '()))
        (else (cond ((= (car PATH) 0) (cond ((f x (car h)) (append (list x (heap-insert-help (cadr h) f (car h) (cdr PATH))) (cddr h)))
                                            (else (append (list (car h) (heap-insert-help (cadr h) f x (cdr PATH))) (cddr h)))))
                    (else (cond ((f x (car h)) (list x (cadr h)) (heap-insert-help (caddr h) f (car h) (cdr PATH)))
                                (else (list (car h) (cadr h) (heap-insert-help (caddr h) f x (cdr PATH))))))))))

(define (heap-path SIZE PATH)
  (cond ((< SIZE 2) PATH)
        (else (heap-path (quotient SIZE 2) (cons (remainder SIZE 2) PATH)))))

(define (heap-peek h)
  (car h))

(define (heap-contains h eq x)
  (cond ((null? h) #f)
        ((eq (car h) x) #t)
        (else (or (heap-contains (cadr h) eq x) (heap-contains (caddr h) eq x)))))

(define (list->heap xs f)
  (list->heap-help (heap-create) xs f))

(define (list->heap-help h xs f)
  (if (null? xs) h (list->heap-help (heap-insert h f (car xs)) (cdr xs) f)))

(provide heap-create heap-is-empty? heap-size heap-insert heap-peek heap-contains list->heap)

