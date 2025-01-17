import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

import {Observable, of, map} from 'rxjs';
import {catchError, filter, tap} from 'rxjs/operators';

import {Room, RoomList, URLLibrary} from '../Container';
import {MessageService} from '../message.service';
import { UpdateService } from '../update.service';


@Injectable({providedIn: 'root'})
export class RoomService {

    constructor(
        private http: HttpClient,
        private messageService: MessageService,
        private updateService: UpdateService) {
    }


    getRoomUpdates(): Observable<RoomList|Room> {
        return this.updateService.updateSubject.pipe(
            tap(console.log),
            map(event => JSON.parse(event.data)),
            map(data => {
                if (data.whatChanged === "ROOM_LIST") {
                    return Object.assign(new RoomList(), data.change);
                } else if (data.whatChanged === "ROOM") {
                    return Object.assign(new Room(), data.change);
                } else {
                    return null;
                }
            }),
            filter(data => data !== null),
            tap(console.log),
        )
    }

    getRoomList(): Observable<RoomList> {
        return this.http.get<RoomList>(URLLibrary.API_URL_ROOMS)
            .pipe(
                tap(_ => this.log('fetched rooms')),
                catchError(this.handleError<RoomList>('getRoomList', new RoomList()))
            );
    }

    /** GET room by id. Will 404 if id not found */
    getRoom(roomRef: string): Observable<Room> {
        const url = `${URLLibrary.API_URL_ROOMS}/${roomRef}`;
        return this.http.get<Room>(url).pipe(
            tap(_ => this.log(`fetched room roomRef=${roomRef}`)),
            catchError(this.handleError<Room>(`getRoom roomRef=${roomRef}`))
        );
    }

    /**
     * Handle Http operation that failed.
     * Let the app continue.
     *
     * @param operation - name of the operation that failed
     * @param result - optional value to return as the observable result
     */
    private handleError<T>(operation = 'operation', result?: T) {
        return (error: any): Observable<T> => {

            console.error(error); // log to console instead

            // TODO: better job of transforming error for user consumption
            this.log(`${operation} failed: ${error.message}`);

            // Let the app keep running by returning an empty result.
            return of(result as T);
        };
    }

    /** Log a RoomService message with the MessageService */
    private log(message: string) {
        this.messageService.add(`RoomService: ${message}`);
    }
}
