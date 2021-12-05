import { IReservation } from '@/shared/model/reservation.model';

import { Nationality } from '@/shared/model/enumerations/nationality.model';
export interface IClient {
  id?: number;
  firstName?: string;
  lastName?: string;
  phone?: string | null;
  email?: string | null;
  nationality?: Nationality | null;
  reservationLists?: IReservation[] | null;
}

export class Client implements IClient {
  constructor(
    public id?: number,
    public firstName?: string,
    public lastName?: string,
    public phone?: string | null,
    public email?: string | null,
    public nationality?: Nationality | null,
    public reservationLists?: IReservation[] | null
  ) {}
}
