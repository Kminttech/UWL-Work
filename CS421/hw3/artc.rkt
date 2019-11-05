#lang racket

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; UTILITY FUNCTIONS
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Converts a scheme-expression into a string
;; INPUT: a scheme-expression EXP
;; OUTPUT: a SCHEME String corresponding to EXP
(define (exp->string exp)
  (cond ((number? exp) (number->string exp))
        ((symbol? exp) (symbol->string exp))
        ((list? exp) (exp->string (car exp)))))

;; INPUT: a list of lists
;; OUTPUT: a list containing all elements of the first-level lists
(define (flatten list-of-lists)
  (cond ((null? list-of-lists) '())
        (else (append (car list-of-lists) (flatten (cdr list-of-lists))))))

;; this is for all error handling.
;; programmers don't use this function but
;; the interpreter calls this function to
;; signal some type of programmer error
(define (error msg)
  (display "ERROR: ")
  (display msg)
  (newline))

;; THERE ARE TWO SUPPORTED TYPES: 'int and 'boolean
;; INPUT: an element of the ART-C language
;; OUTPUT: the type of that element
(define (type-of val)
  (cond ((number? val) 'int)
        ((boolean? val) 'boolean)))

;; A MAP is a list of key-value pairs
;; INPUT: a MAP and a KEY
;; OUTPUT: The value associated with the key or 'error
(define (map-get map x)
  (cond ((null? map) 'error)
        ((equal? (car (car map)) x) (cadr (car map)))
        (else (map-get (cdr map) x))))

;; INPUT : A MAP AND KEY
;; OUTPUT : true if the key is in the map and false otherwise
(define (map-contains map x)
  (cond ((null? map) #f)
        ((equal? (car (car map)) x) #t)
        (else (map-contains (cdr map) x))))

;; INPUT : A MAP, KEY and VALUE
;; OUTPUT: The map that results from replacing the key with the new value.  If
;; the map doesn't contain KEY, then 'error is returned
(define (map-replace map key val)
  (cond ((null? map) 'error)
        ((equal? (car (car map)) key)
         (cons (list key val) (cdr map)))
        (else
         (cons (car map) (map-replace (cdr map) key val)))))

;; INPUT : A MAP, Key and Value
;; OUTPUT : The map that results from adding a key-value pair.  This
;; allows for duplicate keys (the most-recently added is nearer the front of the list
(define (map-add map key val)
  (cons (list key val) map))

;; INPUT: A MAP and KEY
;; OUTPUT: The map that results from deleting the key.  No errors occur if the map
;; doesn't contain the key
(define (map-delete map key)
  (cond ((null? map) map)
        ((equal? (car (car map)) key) (cdr map))
        (else (cons (car map)
                    (map-delete (cdr map) key)))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; TYPEMAP : A SEMANTIC DOMAIN DATA TYPE
;; A typemap is a list of block-level declarations.
;; FORM: ((var1 type1) (var2 type2) (var3 type3) ... )
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; INPUT: NONE
;; OUTPUT: AN empty typemap
(define (typemap-create-empty) '())

;; INPUT: A TYPEMAP
;; OUTPUT: The type of variable x
(define (typemap-type-of tm x)
  (map-get tm x))

;; INPUT: A TYPEMAP
;; OUTPUT: THE TYPEMAP THAT RESULTS FROM INSERTING A DECLARATIONS
(define (typemap-add tm decl)
  (map-add tm (car decl) (cadr decl)))

(define (typemap-delete tm key)
  (map-delete tm key))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; STATE : A SEMANTIC DOMAIN DATA TYPE
;; A LIST OF (VAR, VALUE) pairs
;; FORM :  ( (var1 val1) (var2 val2) ... )
;; NOTE: A map can contain duplicate keys but innermost KEYS occur
;;       before outermost KEYS and hide them
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; INPUT : NONE
;; OUTPUT: AN EMPTY STATE
(define (state-create-empty) '())
  
;; INPUT: STATE and ID
;; OUTPUT: a new state such that the innermost scope now contains a
;;         new binding for the specified ID.  The bindings value is 'undefined.
(define (state-add state id)
  (map-add state id 'undefined))

;; INPUT : STATE and ID
;; OUTPUT: A new state such that the innermost id is removed
(define (state-delete state id)
  (map-delete state id))

;; INPUT: STATE and ID
;; OUTPUT: The value associated with the specified ID in the given state
(define (state-get-value state id)
  (map-get state id))

;; INPUT: STATE and ID
;; OUTPUT: A new state that results from changing the mapping from id->value in
;;         the specified state
(define (state-update state id value)
  (map-replace state id value))

;; INPUT: STATE and LIST-OF-IDS (VARIABLES)
;; OUTPUT: A new state that results from deleting all ids (the variables) from
;;         the specified state
(define (state-delete-all state variables)
  (cond ((null? variables) state)
        (else (state-delete-all (state-delete state (car variables)) (cdr variables)))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; THESE CLASSES CORRESPOND TO THE ABSTRACT SYNTAX SUCH THAT A "PROGRAM"
;; REPRESENTS A PARSE-TREE.  THESE FUNCTIONS OPERATE AT THE 'SYNTACTIC' LEVEL
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; (PROGRAM BODY)
(define (program-get-body stmt)
  (cadr stmt))

;; (BLOCK S1...SN)
(define (block-get-body stmt)
  (filter (lambda (x) (not (is-declaration? x))) (cdr stmt)))
  
(define (block-get-declarations stmt)
  (filter (lambda (x) (is-declaration? x)) (cdr stmt)))

;; (DECLARE TYPE VAR)
(define (declaration-get-type stmt)
  (cadr stmt))

(define (declaration-get-var stmt)
  (caddr stmt))

(define (is-declaration? stmt)
  (and (list? stmt) 
       (equal? (car stmt) 'declare)))

;; (:= VAR EXP)
(define (assignment-get-var stmt)
  (cadr stmt))

(define (assignment-get-exp stmt)
  (caddr stmt))

;; (IF TEST THEN [ELSE])
(define (if-get-test stmt)
  (cadr stmt))

(define (if-get-then stmt)
  (caddr stmt))

(define (if-has-else? stmt)
  (= (length stmt) 4))

(define (if-get-else stmt)
  (cadddr stmt))

;; (WHILE TEST BODY)
(define (while-get-test stmt)
  (cadr stmt))

(define (while-get-body stmt)
  (caddr stmt))

;; (SPRINT LABEL EXP)
(define (sprint-has-exp? stmt)
  (and (list? stmt)
       (= (length stmt) 3)))

(define (sprint-get-label? stmt)
  (cadr stmt))

(define (sprint-get-exp stmt)
  (caddr stmt))

;; INPUT: an expression EXP
;; OUTPUT: the operator of EXP (an element of ART-C)
(define (exp-get-operator exp)
  (car exp))

;; INPUT: an expression EXP
;; OUTPUT: the left-operand (an expression) of EXP
(define (exp-get-left-operand exp)
  (car (cdr exp)))

;; INPUT: an expression EXP
;; OUTPUT: the exp-get-right-operand (an expression) of EXP
(define (exp-get-right-operand exp)
  (car (cdr (cdr exp))))

;; INPUT: an expression EXP
;; OUTPUT: #t if the expression is a boolean literal and #f otherwise
(define (bool? exp)
  (or (equal? exp 'true)
      (equal? exp 'false)))

;; INPUT: a symbol
;; OUTPUT: #t if the symbol is 'true and #f if it is 'false and 'void' if neither
(define (symbol->bool sym)
  (cond ((equal? sym 'true) #t)
        ((equal? sym 'false) #f)))


;; is-program-valid

(define (is-program-valid? pgm)
  (is-block-valid? (program-get-body pgm) (typemap-create-empty)))

(define (is-block-valid? block tm)
  (let ((next-tm (are-declarations-valid? (block-get-declarations block) tm)))
    (if (equal? next-tm #f) #f (is-body-valid? (block-get-body block) next-tm))))

(define (are-declarations-valid? decls tm)
  (if (null? decls) tm (if (checkReserved (declaration-get-var (car decls))) #f
                              (are-declarations-valid? (cdr decls) (map-add tm (declaration-get-var (car decls)) (declaration-get-type (car decls)))))))

(define (is-body-valid? body tm)
  (if (null? body) #t (if (is-statement-valid? (car body) tm)
                          (is-body-valid? (cdr body) tm)
                          #f)))

(define (is-statement-valid? stmt tm)
  (cond ((equal? (car stmt) 'block) (is-block-valid? stmt tm))
        ((equal? (car stmt) ':=) (is-assign-valid? stmt tm))
        ((equal? (car stmt) 'if) (is-if-valid? stmt tm))
        ((equal? (car stmt) 'while) (is-while-valid? stmt tm))
        ((equal? (car stmt) 'sprint) (is-sprint-valid? stmt tm))
        (else #f)))

(define (is-assign-valid? stmt tm)
  (if (equal? (typemap-type-of tm (assignment-get-var stmt)) (type-of-expression (assignment-get-exp stmt) tm)) #t #f))

(define (is-if-valid? stmt tm)
  (if (equal? (type-of-expression (if-get-test stmt) tm) 'boolean) #t #f))

(define (is-while-valid? stmt tm)
  (if (equal? (type-of-expression (while-get-test stmt) tm) 'boolean) #t #f))

(define (is-sprint-valid? stmt tm)
  #t)

(define (checkReserved var)
  (or (equal? var 'program) (equal? var 'block) (equal? var 'declare) (equal? var 'if)
      (equal? var 'while) (equal? var 'sprint) (equal? var 'boolean) (equal? var 'int)
      (equal? var 'true) (equal? var 'false)))

(define (type-of-expression exp tm)
  (if (integer? exp) 'int
      (if (bool? exp) 'boolean
          (if (map-contains tm exp) (typemap-type-of tm exp)
              (let ((op (car exp)))
                (if (or (equal? op '+) (equal? op '-) (equal? op '*) (equal? op '/)
                      (equal? op '@) (equal? op '?) (equal? op '-)) 'int 'boolean))))))
                      
;; INPUT: A PROGRAM
;; A PROGRAM has syntactic structure (program stmt)
;; OUTPUT: THE STATE that results from executing the program
;;         in an empty state.
(define (interpret-program pgm)
  (interpret (program-get-body pgm) (state-create-empty) (typemap-create-empty)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This is the INTERPRETER class
;; An INTERPRETER is simply a collection of functions that
;; operates on TYPES, STATES, BINDING, SCOPES and PROGRAMS
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; INPUT: STATEMENT and STATE
;; OUTPUT: The state that results from executing STATEMENT in STATE
(define (interpret stmt state tm)
  (display stmt) (newline) (display state) (newline)
  (let ((kind (car stmt)))
    (cond ((equal? kind 'block) (interpret-block stmt state tm))
          ((equal? kind 'declare) (interpret-declarations stmt state tm))
          ((equal? kind ':=) (if (is-assign-valid? stmt tm) (interpret-assignment stmt state tm) ((error "invalid assign") #f)))
          ((equal? kind 'if) (if (is-if-valid? stmt tm) (interpret-if stmt state tm) ((error "invalid if") #f)))
          ((equal? kind 'sprint) (if (is-sprint-valid? stmt tm) (interpret-sprint stmt state tm) ((error "invalid sprint") #f)))
          ((equal? kind 'while) (if (is-while-valid? stmt tm) (interpret-while stmt state tm) ((error "invalid while") #f)))       
          (else (error (string-append "statement expected but saw (" (exp->string stmt) "...) instead."))))))

(define (interpret-block block state tm)
  (let ((next-state (interpret-declarations (block-get-declarations block) state)) (remove-decs (get-dec-ids (block-get-declarations block))) (next-tm (are-declarations-valid? (block-get-declarations block) tm)))
    (if (equal? next-tm #f) #f (state-delete-all (interpret-body (block-get-body block) next-state next-tm) remove-decs))))

(define (interpret-declarations decls state)
  (if (null? decls) state (interpret-declarations (cdr decls) (state-add state (declaration-get-var (car decls))))))

(define (get-dec-ids decls)
  (if (null? decls) '() (cons (declaration-get-var (car decls)) (get-dec-ids (cdr decls)))))

(define (interpret-body body state tm)
  (if (null? body) state (if (is-statement-valid? (car body) tm)
                          (interpret-body (cdr body) (interpret (car body) state tm) tm)
                          #f)))

(define (interpret-assignment stmt state tm)
  (state-update state (assignment-get-var stmt) (value-of-expression (assignment-get-exp stmt) state)))

(define (interpret-if stmt state tm)
  (if (value-of-expression (if-get-test stmt) state)
      (interpret (if-get-then stmt) state tm)
      (if (if-has-else? stmt state tm)
          (interpret (if-get-else stmt) state tm)
          state)))

(define (interpret-sprint stmt state tm)
  (display (sprint-get-label? stmt))
  (if (sprint-has-exp? stmt)
      (display (exp->string (value-of-expression (sprint-get-exp stmt) state)))
      (display (exp->string state)))
  (newline)
  state)

(define (interpret-while stmt state tm)
  (if (value-of-expression (while-get-test stmt) state)
      (interpret-while stmt (interpret (while-get-body stmt) state tm) tm)
      state))

(define (value-of-expression exp state)
  (if (integer? exp) exp
      (if (bool? exp) exp
          (if (map-contains state exp) (state-get-value state exp)
              (let ((op (car exp)))
                (if (or (equal? op '+) (equal? op '-) (equal? op '*) (equal? op '/)
                      (equal? op '@) (equal? op '?) (equal? op '-))
                    (evaluate-int-exp exp state)
                    (evaluate-bool-exp exp state)))))))

(define (evaluate-int-exp exp state)
  (let ((op (exp-get-operator exp)))
    (cond ((equal? op '+) (+ (value-of-expression (exp-get-left-operand exp) state) (value-of-expression (exp-get-right-operand exp) state)))
          ((equal? op '-) (if (= (length exp) 2) (- 0 (value-of-expression (exp-get-left-operand exp) state))
                              (- (value-of-expression (exp-get-left-operand exp) state) (value-of-expression (exp-get-right-operand exp) state))))
          ((equal? op '*) (* (value-of-expression (exp-get-left-operand exp) state) (value-of-expression (exp-get-right-operand exp) state)))
          ((equal? op '/) (/ (value-of-expression (exp-get-left-operand exp) state) (value-of-expression (exp-get-right-operand exp) state)))
          ((equal? op '@) (expt (value-of-expression (exp-get-left-operand exp) state) (value-of-expression (exp-get-right-operand exp) state)))
          ((equal? op '?) (remainder (value-of-expression (exp-get-left-operand exp) state) (value-of-expression (exp-get-right-operand exp) state))))))

(define (evaluate-bool-exp exp state)
  (let ((op (exp-get-operator exp)))
    (cond ((equal? op '<) (< (value-of-expression (exp-get-left-operand exp) state) (value-of-expression (exp-get-right-operand exp) state)))
          ((equal? op '>) (> (value-of-expression (exp-get-left-operand exp) state) (value-of-expression (exp-get-right-operand exp) state)))
          ((equal? op '=) (= (value-of-expression (exp-get-left-operand exp) state) (value-of-expression (exp-get-right-operand exp) state)))
          ((equal? op '<=) (>= (value-of-expression (exp-get-left-operand exp) state) (value-of-expression (exp-get-right-operand exp) state)))
          ((equal? op '&) (and (value-of-expression (exp-get-left-operand exp) state) (value-of-expression (exp-get-right-operand exp) state)))
          ((equal? op '%) (or (value-of-expression (exp-get-left-operand exp) state) (value-of-expression (exp-get-right-operand exp) state)))
          ((equal? op '~) (not (value-of-expression (exp-get-left-operand exp) state))))))

(define pgm '(program 
              (block
               (declare int n)
               (declare boolean error)
               (declare int result)   
               (:= error false)
               (:= result 1)
               (block 
                (declare int local)
                (:= n 5)
                (:= local n)
                (while (> local 0)
                       (block
                        (:= result (* result local))
                        (:= local (- local 1)))))
              (sprint "result: " result)
              (if (! error) (sprint "a") (sprint "b")))))

(interpret-program pgm)