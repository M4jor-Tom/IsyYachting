import { IReservation } from '@/shared/model/reservation.model';

import { CancelingWeather } from '@/shared/model/enumerations/canceling-weather.model';
export interface IDay {
  id?: number;
  time?: Date;
  forecastedWeather?: CancelingWeather | null;
  reservationLists?: IReservation[] | null;
}

export class Day implements IDay {
  constructor(
    public id?: number,
    public time?: Date,
    public forecastedWeather?: CancelingWeather | null,
    public reservationLists?: IReservation[] | null
  ) {}
}
