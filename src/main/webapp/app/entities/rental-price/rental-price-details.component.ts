import { Component, Vue, Inject } from 'vue-property-decorator';

import { IRentalPrice } from '@/shared/model/rental-price.model';
import RentalPriceService from './rental-price.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class RentalPriceDetails extends Vue {
  @Inject('rentalPriceService') private rentalPriceService: () => RentalPriceService;
  @Inject('alertService') private alertService: () => AlertService;

  public rentalPrice: IRentalPrice = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.rentalPriceId) {
        vm.retrieveRentalPrice(to.params.rentalPriceId);
      }
    });
  }

  public retrieveRentalPrice(rentalPriceId) {
    this.rentalPriceService()
      .find(rentalPriceId)
      .then(res => {
        this.rentalPrice = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
