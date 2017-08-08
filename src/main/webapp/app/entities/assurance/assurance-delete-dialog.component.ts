import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Assurance } from './assurance.model';
import { AssurancePopupService } from './assurance-popup.service';
import { AssuranceService } from './assurance.service';

@Component({
    selector: 'jhi-assurance-delete-dialog',
    templateUrl: './assurance-delete-dialog.component.html'
})
export class AssuranceDeleteDialogComponent {

    assurance: Assurance;

    constructor(
        private assuranceService: AssuranceService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.assuranceService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'assuranceListModification',
                content: 'Deleted an assurance'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-assurance-delete-popup',
    template: ''
})
export class AssuranceDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private assurancePopupService: AssurancePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.assurancePopupService
                .open(AssuranceDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
