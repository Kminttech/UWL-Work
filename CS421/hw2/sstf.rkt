#lang racket

(define (sstf L N)
  (sstf-helper L N (length L) 0 '()))

(define (sstf-helper L N LENGTH TOTAL OUT)
  (cond ((null? L) (list (/ TOTAL LENGTH) TOTAL OUT))
        (else (let ((next (find-shortest (cdr L) N (car L))))
          (sstf-helper (removeElement L next) next LENGTH (+ TOTAL (abs (- next N))) (append OUT (list (abs (- next N)))))))))

(define (find-shortest L N C)
  (cond ((null? L) C)
        ((< (abs (- (car L) N)) (abs (- C N))) (find-shortest (cdr L) N (car L)))
        (else (find-shortest (cdr L) N C))))

(define (removeElement L E)
  (cond ((null? L) '())
        ((= (car L) E) (cdr L))
        (else (cons (car L) (removeElement (cdr L) E)))))

(provide sstf)