grows([]).
grows([_]).
grows([X,Y|Z]) :- X =< Y , grows([Y|Z]).

removed([],[]).
removed([H|T], List) :-    
	member(H, T),
	removed(T, List).
removed([H|T], [H|T1]) :- 
	\+member(H, T),
	removed(T, T1).

set_intersection([], _, []).
set_intersection([H1|T1], L2, [H1|Res]) :-
    member(H1, L2),
    set_intersection(T1, L2, Res).
set_intersection([_|T1], L2, Res) :-
    set_intersection(T1, L2, Res).

running(L, R) :- reverse(L, RL), run_help(RL, RSL), reverse(RSL, R).
run_help([X], [X]).
run_help([H|T], L) :- sum_list([H|T], S), run_help(T, LS), append([S], LS, L).

list_merge([],L,L).
list_merge(L,[],L).
list_merge([H1|T1], [H2|T2], L) :- 
    H1 =< H2 -> L = [H1|R], list_merge(T1,[H2|T2],R) ;
    H1 > H2 -> L = [H2|R], list_merge([H1|T1],T2,R) .

dependsOn( lib0, []).
dependsOn( lib1, []).
dependsOn( lib2, []).
dependsOn( lib3, []).
dependsOn( lib4, [lib3] ).
dependsOn( lib5, [lib0, lib2] ).
dependsOn( lib6, [lib5, lib3] ).
dependsOn( lib7, [lib1, lib3, lib4 ] ).
dependsOn( lib8, [lib3, lib5] ).

imports([],[]).
imports([X|XS], IMPORTS) :- dependsOn(X, DEP), imports(XS, RETURN), append(RETURN, DEP, IMPORTS).
imports(LIB, IMPORTS) :- dependsOn(LIB, [X|XS]), imports([X|XS], RETURN), append(RETURN, [X|XS], TEMP), removed(TEMP, IMPORTS).