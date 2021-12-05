import { IDay } from '@/shared/model/day.model';
import { IClient } from '@/shared/model/client.model';
import { IRental } from '@/shared/model/rental.model';

export interface IReservation {
  id?: number;
  day?: IDay;
  client?: IClient;
  rental?: IRental;
}

export class Reservation implements IReservation {
  constructor(public id?: number, public day?: IDay, public client?: IClient, public rental?: IRental) {}
}
